# trades-profiler-engine
Aggregates a series of trade level input data in Open High Low Close json format in multi interval sets of 15 seconds

#Input Format
{"sym":"ANIMESH", "T":"Trade",  "P":6199.4, "Q":0.17462, "TS":1539440608.5583, "side": "s", "TS2":1539440614091077565}
{"sym":"ANIMESH", "T":"Trade",  "P":6199.4, "Q":0.00044993, "TS":1539440608.5659, "side": "s", "TS2":1539440614091130258}
{"sym":"ANIMESH", "T":"Trade",  "P":6200, "Q":0.1376, "TS":1539440622.6547, "side": "b", "TS2":1539440632641558971}

#Aggregated Output
INFO {"o":196.89,"h":196.89,"l":196.89,"c":0,"volume":0.9905,"event":"ohlc_event","symbol":"ANIMESH","barnum":14934}
INFO {"o":196.89,"h":196.89,"l":196.89,"c":196.89,"volume":1.9810,"event":"ohlc_event","symbol":"ANIMESH","barnum":14934}
INFO {"o":6199.4,"h":6199.4,"l":6199.4,"c":0,"volume":0.17462,"event":"ohlc_event","symbol":"ANIMESH","barnum":14935}
INFO {"o":6199.4,"h":6199.4,"l":6199.4,"c":6199.4,"volume":0.17506993,"event":"ohlc_event","symbol":"ANIMESH","barnum":14935}
INFO {"o":6200,"h":6200,"l":6200,"c":6200,"volume":0.1376,"event":"ohlc_event","symbol":"ANIMESH","barnum":14936}

Please note that it aggregates against 15 seconds batch and counts every batch and names it against the barnum field,
see it moves from 14935 to 14936 after 15 seconds expires

#TS2 is used as to measure time in nano-seconds 1s = 10^9 s

Please clone the repo and open the project on intellij ide

Instructions to run the jar on linux / macOs
-> go to build/libs/trade-profiler-engine-all-Dependencies-1.0.jar and download this jar
-> open any unix/linux terminal and then run the below

java -jar trade-profiler-engine-all-Dependencies-1.0.jar

Optionally, you can redirect the generated output to any output files or pipe it to filter-grep as per your requirements.
