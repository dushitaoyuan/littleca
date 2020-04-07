#!/usr/bin/env bash
#!/bin/bash
JAVA_OPS=""
APP_NAME=`ls ./| grep .jar$`
function check {
    local pid=`ps aux | grep $APP_NAME|grep -v "grep" | grep  "java" | awk '{ print $2 }'`
    if [ -z $pid ];then
       echo 0
    else
      echo $pid
    fi
}
function isRun {
    local pid=$(check)
    if [ $pid -ne 0 ];then
        echo "$APP_NAME is runing,pid is $pid"
    else
	    echo "$APP_NAME not run"
    fi
}
function start {
    local pid=$(check)
    if [ $pid -ne 0 ];then
        echo "$APP_NAME is runing pid $pid"
	return 0
    fi
    nohup java -jar $JAVA_OPS $APP_NAME  > log.out 2>&1 &
    pid=$(check)
    echo "$APP_NAME pid is $pid"
}

function softStop {
    local pid=$(check)
    if [ $pid -ne 0 ] ;then
        kill -15 $pid
        num=0
        while [[ num -le 5 ]]; do
            sleep 1
            pid=$(check)
            if [ $pid -eq 0 ];then
                break;
            fi
            num=$[ $num +1 ]
        done
    fi
}
# 慎用 kill 9 如果kill -15 杀不死使用kill -9
function foreStop {
    local pid=$(check)
    if [ $pid -ne 0 ] ;then
        kill -9 $pid
    fi
}
function stop() {
    local pid=$(check)
    softStop
    if [ $pid -ne 0 ] ;then
        echo "$APP_NAME is runing,pid is $pid"
        echo "stop $APP_NAME  success"
    else
        sleep 1
        if [ $pid -ne 0 ] ;then
            foreStop
            echo "stop $APP_NAME  success"
        fi
    fi


}
function restart {
    stop
    start
}


case $1 in
    "start" ) start;;
    "stop" ) stop;;
    "isRun" ) isRun;;
    "restart" ) restart;;
    * )  echo "Usage: $0 {start|stop|isRun|restart}";;
esac
exit 0


