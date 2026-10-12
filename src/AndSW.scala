import circt.stage.ChiselStage
import chisel3._
import chisel3.util._
import java.nio.file.{Files, Paths}

class AndSW extends Module {
  val io = IO(new Bundle {
    val sw = Input(UInt(16.W))
    val led = Output(UInt(16.W))
  })

  val swReg = RegInit(0.U(16.W))
  val ledReg = RegInit(0.U(16.W))

  when(swReg(0) === 1.U && swReg(1) === 1.U) {
    ledReg := 1.U
  }.otherwise { ledReg := 0.U }

  io.led := ledReg
  swReg := io.sw
}
