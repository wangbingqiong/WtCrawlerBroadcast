#Æô¶¯ÅÀ³æ

echo start crawl
_heapMem="-Xms256m -Xmx512m"
#java $_heapMem -jar WtCrawlerBroadcast.jar
nohup java $_heapMem -jar WtCrawlerBroadcast.jar > crawler.log 2>&1 &
