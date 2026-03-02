import circt.stage.ChiselStage
import chisel3._
import chisel3.util._
import java.nio.file.{Files, Paths}

class Blinky(clockHz: Int, blinkHz: Int = 1) extends Module {
  require(clockHz >= 2 * blinkHz, "clock too slow for this blink rate")

  val io = IO(new Bundle {
    val led = Output(Bool())
  })

  val maxCount = clockHz / (blinkHz * 2) - 1

  val counter = RegInit(0.U(log2Up(maxCount + 1).W))
  val ledReg = RegInit(false.B)

  when(counter === maxCount.U) {
    counter := 0.U
    ledReg := !ledReg
  }.otherwise {
    counter := counter + 1.U
  }

  io.led := ledReg
}
