#!/bin/bash
for line in `cat memguid`
do
    echo ${line} >>result.json
    cache='key=avaliablescore:'+${line}
    curl -H "Content-type: application/json" -X POST -d ${cache} http://score.idc1.fn/FeiniuScore/growth/cleanCacheValue
    echo ":">>result.json
    curl  http://score.idc1.fn/FeiniuScore/v1/score/getUserAvaliableScore?memGuid=${line} >> result.json
    echo "\n" >>result.json
done

