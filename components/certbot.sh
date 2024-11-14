#!/bin/bash

# Nginx 설치
echo "Nginx를 설치합니다..."
sudo dnf install -y nginx

# Certbot과 종속성 설치
echo "python3와 augeas-libs를 설치합니다..."
sudo dnf install -y python3 augeas-libs

# Certbot 가상 환경 생성
echo "Certbot 가상 환경을 생성합니다..."
sudo python3 -m venv /opt/certbot/
sudo /opt/certbot/bin/pip install --upgrade pip
sudo /opt/certbot/bin/pip install certbot certbot-nginx

# Certbot 링크 생성
echo "Certbot 실행 파일 링크를 생성합니다..."
sudo ln -s /opt/certbot/bin/certbot /usr/bin/certbot

# 인증서 발급
echo "인증서를 발급합니다..."
sudo certbot --nginx

# 인증서 파일을 사용자 디렉토리로 이동
echo "인증서 파일을 ~/certbot/conf로 이동합니다..."
mkdir -p ~/certbot/conf
sudo mv /etc/letsencrypt/* ~/certbot/conf

# Nginx 제거
echo "Nginx를 제거합니다..."
sudo dnf remove -y nginx

sudo rm -rf /etc/nginx
sudo rm -rf /var/log/nginx
sudo rm -rf /var/cache/nginx

echo "모든 과정이 완료되었습니다."
