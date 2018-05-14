#!/usr/bin/env bash
mysql_passwd=v9jOBbSSWNQzMZEY

apt-get update
apt-get install -y apt-transport-https

wget https://get.docker.com -O get.sh
chmod +x get.sh
./get.sh --mirror Aliyun

mkdir -p /etc/docker
tee /etc/docker/daemon.json <<-'EOF'
{
  "registry-mirrors": ["https://dpzpmu1h.mirror.aliyuncs.com"]
}
EOF
systemctl daemon-reload
systemctl restart docker

tee /home/data/nginx/conf.d/nginx.conf <<-'EOF'
server_tokens off;

server{
    listen 80 default_server;

    keepalive_timeout   60;

    location / {
        proxy_pass   http://tomcat:8080/;
        proxy_redirect  off;
        proxy_set_header  Host $host:$server_port;
        proxy_set_header  X-Real-IP $remote_addr;
        proxy_set_header  X-Forwarded-For $proxy_add_x_forwarded_for;
    }
}
EOF

docker run -dp 3306:3306 --restart=always -e MYSQL_ROOT_PASSWORD=$mysql_passwd -v /home/data/mysql:/var/lib/mysql --name mysql mysql
docker run -dp 8080:8080 --restart=always --link mysql:mysql -v /home/data/tomcat/webapps:/usr/local/tomcat/webapps --name tomcat tomcat:alpine
docker run -dp 80:80 -p 443:443 --restart=always --link tomcat:tomcat -v /home/data/nginx/conf.d:/etc/nginx/conf.d -v /home/data/nginx/home:/home --name nginx nginx:alpine
