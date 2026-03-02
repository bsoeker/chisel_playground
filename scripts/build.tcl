set part   xc7a100tcsg324-1     ;# Nexys A7-100T. For the A7-50T: xc7a50ticsg324-1L
set top    NexysTop
set outDir vivado_out

file mkdir $outDir

read_verilog -sv generated/NexysTop.sv
read_xdc constraints/Nexys-A7-100T-Master.xdc

synth_design -top $top -part $part
opt_design
place_design
route_design

report_timing_summary -file $outDir/timing.rpt
report_utilization    -file $outDir/utilization.rpt

write_bitstream -force $outDir/$top.bit
