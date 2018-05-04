docker ps -a
docker stop $(docker ps -a -q)
docker rm $(docker ps -a -q)
docker rmi luoruofeng/stock:latest
echo "删除所有docker容器,删除stock的docker!"


echo "新建存放CSV的文件夹"
rm -rf /stock
mkdir -p  -m 777 /stock/data


echo "创建mongo data db文件夹"
rm -rf /stockmongo/data/db
mkdir -p -m 777 /stockmongo/data/db

echo "启动mongo的docker容器"

  

echo "创建Stock的Docker镜像"
docker run --name stock-mongo -p 27017:27017 -v /stockmongo/data/db:/data/db -d mongo && mvn clean install dockerfile:build -Dmaven.test.skip=true && docker run --link stock-mongo:mongo -p 8080:8080 --name stock -it luoruofeng/stock:latest init --spring.profiles.active=prod