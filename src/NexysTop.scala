import chisel3._

class NexysTop extends RawModule {
  val CLK100MHZ = IO(Input(Clock()))
  val CPU_RESETN = IO(Input(Bool()))
  val LED = IO(Output(UInt(16.W)))
  val SW = IO(Input(UInt(16.W)))

  withClockAndReset(CLK100MHZ, !CPU_RESETN) {
    val andSw = Module(new AndSW())
    LED := andSw.io.led
    andSw.io.sw := SW
  }
}

object Main extends App {
  Emit(new NexysTop, "NexysTop.sv")
}
