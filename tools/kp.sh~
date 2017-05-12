#!/bin/bash
#kill expDroid process who use the port 5050  
res=`netstat -anp | grep "5050"| grep "java"| head -n 1` 
if test -z $res 
   then echo "the proces died"
else 
   a=`netstat -anp | grep "5050"| grep "java"| head -n 1| awk '{print $7}' |awk -F '/' '{print $1}'`;
   kill -9 $a
   echo "kill process $a"
fi

# kill expDroidClient process in android device
exist=`adb shell ps| grep "exp.process"|head -n 1|awk '{print $2}'`
if test -z $exist 
   then echo "process of exp died"
else 
   pid=`adb shell ps| grep "exp.process"|head -n 1|awk '{print $2}'`
   adb shell kill -9 $pid
   echo "kill $pid in android device"
fi


# kill expDroid process in android device
exist=`ps -aux| grep "java -ea -class"|head -n 1|awk '{print $2}'`
if test -z $exist 
   then echo "process of exp died"
else 
   pid=`ps -aux| grep "java -ea -class"|head -n 1|awk '{print $2}'`
   kill -9 $pid
   echo "kill $pid in PC"
fi

