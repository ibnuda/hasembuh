package apriori

/**
 * Pretty much taken shamelessly from folone's work
 * github.com/folone/poi.scala
 */
//import org.apache.poi.ss.usermodel.Sheet

import java.io.{File, FileOutputStream}

import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.{Row => POIRow, Cell => POICell, Workbook => POIWorkbook, WorkbookFactory}
import org.apache.poi.xssf.usermodel.XSSFWorkbook

import scalaz._
import std.option._
import std.map._
import std.list._
import syntax.applicative._
import syntax.monoid._
import syntax.std.option._
import syntax.std.all._
import effect.IO

class Eksel(val sheetMap: Map[String, Sheet], format: WorkbookVersion) {
	val sheets: Set[Sheet] = sheetMap.values.toSet

	private def setPoiCell(row: POIRow, cell: Cell, poiCell: POICell): Unit = {
		cell match {
			case StringCell(index, data) => poiCell.setCellValue(data)
			case NumericCell(index, data) => poiCell.setCellValue(data)
			case FormulaCell(index, data) => poiCell.setCellFormula(data)
		}
	}

	private lazy val book = {
		val workbook = format match {
			case HSSF => new HSSFWorkbook
			case XSSF => new XSSFWorkbook
		}
		sheets.foreach { sh =>
			val Sheet((name), (rows)) = sh
			val sheet = workbook.createSheet(name)
			rows.foreach { rw =>
				val Row((index), (cells)) = rw
				val row = sheet.createRow(index)
				cells.foreach { cl =>
					val poiCell = row.createCell(cl.index)
					setPoiCell(row, cl, poiCell)
				}
			}
		}
		workbook
	}

	def safeToFile(path: String): EitherT[IO, Throwable, Unit] = {
		def close(resource: {
			def close(): Unit
		}): IO[Unit] = IO {
			resource.close()
		}
		val action = IO {
			new FileOutputStream(new File(path))
		}.bracket(close) { file =>
			IO {
				book.write(file)
			}
		}
		EitherT(action.catchLeft)
	}

	def asPoi: POIWorkbook = book

	override def toString = Show[Eksel].shows(this)
	override def equals(obj: Any): Boolean =
		obj != null && obj.isInstanceOf[Eksel] && Equal[Eksel].equal(obj.asInstanceOf[Eksel], this)

}

object Eksel {
	def apply(sheets: Set[Sheet], format: WorkbookVersion = HSSF): Eksel =
		new Eksel(sheets.map(s => (s.name, s)).toMap, format)

	def apply(path: String) = {
		val action: IO[File] = IO {
			new File(path)
		}
		//EitherT((action <*> fromFile(HSSF)).catchLeft)
	}

	def fromFile(format: WorkbookVersion) = readWorkbook[File](format, f => WorkbookFactory.create(f))

	def readWorkbook[T](format: WorkbookVersion, workbookFile: T => POIWorkbook) = IO { is: T =>
		val wb = workbookFile(is)
		val data = for {
			i <- 0.until(wb.getNumberOfSheets)
			sheet = wb.getSheetAt(i) if (sheet != null)
			k <- 0.to(sheet.getLastRowNum)
			row = sheet.getRow(k) if (row != null)
			j <- 0.until(row.getLastCellNum)
			cell = row.getCell(j) if (cell != null)
		} yield (sheet, row, cell)
		val result = data.groupBy(_._1).mapValues { list =>
			list.map {
				case (s, r, c) => (r, c)
			}.groupBy(_._1)
			.mapValues(l => l.map {
				case (r, c) => c
			}.toList)
		}
		val sheets = result.map{ case (sheet, rowList) =>
				Sheet(sheet.getSheetName) {
					rowList.map { case (row, cellList) =>
							Row(row.getRowNum) {
								cellList.flatMap { cell =>
									val index = cell.getColumnIndex
									cell.getCellType match {
										case POICell.CELL_TYPE_STRING => Some(StringCell(index, cell.getStringCellValue))
										case POICell.CELL_TYPE_NUMERIC => Some(NumericCell(index, cell.getNumericCellValue))
										case POICell.CELL_TYPE_FORMULA => Some(FormulaCell(index, cell.getCellFormula))
									}
								}.toSet
							}
					}.toSet
				}
		}.toSet
		Eksel(sheets)
	}
}

class Sheet(val name: String)(val rows: Set[Row]) {
	override def toString: String = Show[Sheet].shows(this)
	override def equals(obj: Any): Boolean =
		obj != null && obj.isInstanceOf[Sheet] && Equal[Sheet].equal(obj.asInstanceOf[Sheet], this)
}
object Sheet {
	def apply(name: String)(rows: Set[Row]): Sheet = new Sheet(name)(rows)
	def unapply(sheet: Sheet): Option[(String, Set[Row])] = Some((sheet.name, sheet.rows))
}

class Row(val index: Int)(val cells: Set[Cell]) {
	override def toString: String = Show[Row].shows(this)
	override def equals(obj: Any): Boolean =
		obj != null && obj.isInstanceOf[Row] && Equal[Row].equal(obj.asInstanceOf[Row], this)
}
object Row {
	def apply(index: Int)(cells: Set[Cell]): Row = new Row(index)(cells)
	def unapply(row: Row): Option[(Int, Set[Cell])] = Some((row.index, row.cells))
}

sealed abstract class Cell(val index: Int) {
	override def toString: String = Show[Cell].shows(this)
}

case class StringCell(override val index: Int, data: String) extends Cell(index)
case class NumericCell(override val index: Int, data: Double) extends Cell(index)
class FormulaCell(override val index: Int, val data: String) extends Cell(index) {
	equalities.formulaCellEqualInstance
	override def equals(obj: Any): Boolean =
		obj != null && obj.isInstanceOf[FormulaCell] && Equal[FormulaCell].equal(obj.asInstanceOf[FormulaCell], this)
}
object FormulaCell {
	def apply(index: Int, data: String): FormulaCell = new FormulaCell(index, data.dropWhile(_ == '='))
	def unapply(cell: FormulaCell): Option[(Int, String)] = Some((cell.index, cell.data))
}

object equalities {
	implicit val formulaCellEqualInstance = new Equal[FormulaCell] {
		override def equal(f1: FormulaCell, f2: FormulaCell) = f1.index == f2.index && f1.data == f2.data
	}
	implicit val cellEqualInstance = new Equal[Cell] {
		override def equal(s1: Cell, s2: Cell) = (s1, s2) match {
			case (s1: StringCell, s2: StringCell) => s1 == s2
			case (n1: NumericCell, n2: NumericCell) => n1 == n2
			case (f1: FormulaCell, f2: FormulaCell) => Equal[FormulaCell].equal(f1, f2)
			case (_, _) => false
		}
	}
}
sealed abstract class WorkbookVersion
case object HSSF extends WorkbookVersion
case object XSSF extends WorkbookVersion
