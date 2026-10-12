import chisel3._
import chisel3.simulator.scalatest.ChiselSim
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

/** Tests for [[AndSW]].
  *
  * ChiselSim (built into Chisel 7) elaborates the module, hands it to
  * Verilator and gives you a handle (`dut`) whose ports you drive with
  * `poke`, sample with `peek` and assert on with `expect`.
  *
  * AndSW registers `io.sw` once and then combines combinationally, so a
  * value poked onto `io.sw` appears on `io.led` after a single `step()`.
  */
class AndSWSpec extends AnyFlatSpec with ChiselSim with Matchers {

  behavior of "AndSW"

  it should "light led(0) when both sw(0) and sw(1) are high" in {
    simulate(new AndSW) { dut =>
      dut.io.sw.poke("b11".U)
      dut.clock.step()
      dut.io.led.expect(1.U, "both switches on should drive led = 1")
    }
  }

  it should "keep the led off when only one switch is high" in {
    simulate(new AndSW) { dut =>
      for (sw <- Seq("b01".U, "b10".U)) {
        dut.io.sw.poke(sw)
        dut.clock.step()
        dut.io.led.expect(0.U)
      }
    }
  }

  it should "reset with the led off" in {
    simulate(new AndSW) { dut =>
      // `simulate` already applies a reset; just check the initial output.
      dut.io.led.expect(0.U)
    }
  }

  it should "ignore the upper switches" in {
    simulate(new AndSW) { dut =>
      dut.io.sw.poke("hFFFC".U) // every switch but sw(1 downto 0)
      dut.clock.step()
      dut.io.led.expect(0.U)

      dut.io.sw.poke("hFFFF".U) // ...now add the bottom two
      dut.clock.step()
      dut.io.led.expect(1.U)
    }
  }

  it should "respond after exactly one cycle, rising and falling" in {
    simulate(new AndSW) { dut =>
      dut.io.sw.poke("b11".U)
      dut.io.led.expect(0.U, "led must not react before a clock edge")
      dut.clock.step()
      dut.io.led.expect(1.U, "led reacts on the first cycle")

      dut.io.sw.poke(0.U)
      dut.clock.step()
      dut.io.led.expect(0.U, "...and falls one cycle later too")
    }
  }

  it should "hold its output while the switches are unchanged" in {
    simulate(new AndSW) { dut =>
      dut.io.sw.poke("b11".U)
      dut.clock.step(10)
      dut.io.led.expect(1.U)
    }
  }

  it should "match a software model over a random stimulus" in {
    simulate(new AndSW) { dut =>
      val rng = new scala.util.Random(42)

      for (_ <- 0 until 100) {
        val sw = rng.nextInt(1 << 16)
        dut.io.sw.poke(sw.U)
        dut.clock.step()
        dut.io.led.expect(if ((sw & 3) == 3) 1.U else 0.U)
      }
    }
  }
}
