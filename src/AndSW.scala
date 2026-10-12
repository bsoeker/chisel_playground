import chisel3._
import chisel3.util._

class AndSW extends Module {
  val io = IO(new Bundle {
    val sw = Input(UInt(16.W))
    val led = Output(UInt(16.W))
  })

  // Register the asynchronous switch inputs, then combine combinationally.
  val swReg = RegInit(0.U(16.W))
  swReg := io.sw

  io.led := (swReg(0) && swReg(1)).asUInt
}
