package apriori

import java.io.{File, FileInputStream}

import models.DataRule.{Barang, Transaksi}
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel._
import org.apache.poi.xssf.usermodel.XSSFWorkbook

import scala.collection.immutable.IndexedSeq
import scala.util.matching.Regex


class Eksel() {

	def berkasToTransaksi(berkas: File): List[Transaksi] = {
		val ekstensi: String = ekstensiBerkas(berkas)
		val workbook: Workbook = ekstensi match {
			case "xls" => new HSSFWorkbook(new FileInputStream(berkas))
			case "xlsx" => new XSSFWorkbook(new FileInputStream(berkas))
			case _ => new XSSFWorkbook()
		}
		//workbookToMapped(workbook)
		workbookToListTransaksi(workbook)
	}

	def berkasToBarang(berkas: File): List[Barang] = {
		val workbook: Workbook = ekstensiBerkas(berkas) match {
			case "xls" => new HSSFWorkbook(new FileInputStream(berkas))
      case "xlsx" => new XSSFWorkbook(new FileInputStream(berkas))
      case _ => new XSSFWorkbook()
		}
		workbookToListBarang(workbook)
	}

	def ekstensiBerkas(berkas: File): String = {
		val namaBerkas: String = berkas.getName
		lazy val regexEkstensi: Regex = """^.*\.(.*)?""".r
		val ekstensi: String = namaBerkas match {
			case regexEkstensi(ekst) => ekst
			case _ => "bukan file biasa"
		}
		ekstensi
	}

	def berkasToIndexedSeq(wb: Workbook): IndexedSeq[(Sheet, Row, Cell)] = {
		val data: IndexedSeq[(Sheet, Row, Cell)] = for {
			i <- 0.until(wb.getNumberOfSheets)
			sheet: Sheet = wb.getSheetAt(i) if sheet != null
			k <- 1.to(sheet.getLastRowNum)
			baris: Row = sheet.getRow(k) if baris != null
			j <- 0.until(baris.getLastCellNum.toInt)
			sel: Cell = baris.getCell(j) if sel != null
		} yield (sheet, baris, sel)
		data
	}

	def indexedToMapped(data: IndexedSeq[(Sheet, Row, Cell)]): Map[Sheet, Map[Row, List[Cell]]] = {
		data.groupBy(_._1).mapValues { list =>
			list.map {
				case (sheet, baris, sel) => (baris, sel)
			}.groupBy(_._1).mapValues( l => l.map {
				case (baris, sel) => sel
			}.toList.take(3))
		}
	}

	def mappedToTransaksi(konten: Map[Sheet, Map[Row, List[Cell]]]): List[Transaksi] = konten.map {
    case (sheet, listBaris) =>
      listBaris.map { case (baris, listSel) => {
        val no = listSel(0).getNumericCellValue.toInt
        val id = listSel(1).getNumericCellValue.toInt
        val ba = listSel(2).getNumericCellValue.toInt
        Transaksi(no, id, ba)
      }}.toList
  }.toList.flatten.sortBy(_.no).sortBy(_.idtrans)

	def mappedToBarang(konten: Map[Sheet, Map[Row, List[Cell]]]): List[Barang] = konten.map {
		case (sheet, listBaris) =>
			listBaris.map { case (baris, listSel) => {
				val id = listSel(0).getNumericCellValue.toInt
				val na = listSel(1).getStringCellValue
				val ha = listSel(2).getNumericCellValue.toInt
				Barang(id, na, ha)
			}}.toList
	}.toList.flatten.sortBy(_.idbarang)

	def workbookToMapped(wb: Workbook): Map[Sheet, Map[Row, List[Cell]]] = {
		val data: IndexedSeq[(Sheet, Row, Cell)] = berkasToIndexedSeq(wb)
		val konten: Map[Sheet, Map[Row, List[Cell]]] = indexedToMapped(data)
		konten
	}

	def workbookToListTransaksi(wb: Workbook): List[Transaksi] = {
		val konten = workbookToMapped(wb)
		val listTransaksi = mappedToTransaksi(konten)
		listTransaksi
	}

	def workbookToListBarang(wb: Workbook): List[Barang] = {
		val konten = workbookToMapped(wb)
		val listBarang = mappedToBarang(konten)
		listBarang
	}
}

