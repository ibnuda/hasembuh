package apriori

import java.io.{File, FileInputStream}

import models.daoapriori.DBTableDefinitions.Transaksi
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel._
import org.apache.poi.xssf.usermodel.XSSFWorkbook

import scala.collection.immutable.IndexedSeq


class Eksel() {
	def ekstensiBerkas(berkas: File): String = {
		val namaBerkas = berkas.getName
		lazy val regexEkstensi = """^.*\.(.*)?""".r
		val ekstensi = namaBerkas match {
			case regexEkstensi(ekst) => ekst
			case _ => "bukan file biasa"
		}
		ekstensi
	}

	def loadDariBerkas(berkas: File) = {
		val ekstensi = ekstensiBerkas(berkas)
		val workbook = ekstensi match {
			case "xls" => new HSSFWorkbook(new FileInputStream(berkas))
			case "xlsx" => new XSSFWorkbook(new FileInputStream(berkas))
			case _ => new XSSFWorkbook()
		}
		ekstrakKonten(workbook)
	}

	def ekstrakKonten(wb: Workbook) = {
		val data: IndexedSeq[(Sheet, Row, Cell)] = for {
			i    <- 0.until(wb.getNumberOfSheets)
			sheet = wb.getSheetAt(i) if sheet != null
			k    <- 1.to(sheet.getLastRowNum)
			baris = sheet.getRow(k) if baris != null
			j    <- 0.until(baris.getLastCellNum.toInt)
			sel   = baris.getCell(j) if sel != null
		} yield (sheet, baris, sel)
		val konten: Map[Sheet, Map[Row, List[Cell]]] = data.groupBy(_._1).mapValues { list =>
			list.map {
				case (sheet, baris, sel) => (baris, sel)
			}.groupBy(_._1).mapValues(l => l.map{
				case (baris, sel) => sel
			}.toList.take(3))
		}
		val hasil: List[List[Transaksi]] = konten.map { case (sheet, listBaris) =>
				listBaris.map { case (baris, listSel) => {
					val no = listSel.head.getNumericCellValue
					val id = listSel(1).getNumericCellValue
					val ba = listSel.last.getNumericCellValue
					val transak = Transaksi(no.toInt, id.toInt, ba.toInt)
					transak
				}
			}.toList
		}.toList
		printHasil(hasil)
	}

	def printHasil(hasil: List[List[Transaksi]]): Unit = {
		val superSekali: Unit = hasil.foreach { anu =>
			anu.foreach { anu2 =>
				val aaa = anu2.toString
				print(aaa)
			}
			println()
		}
	}

}

