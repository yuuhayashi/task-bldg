# task-bldg

Plateau 3D都市データをOSMに反映させるための「タスク管理」ポータル

このWebアプリケーションではPlateauで配布されているファイル単位を「タスク」とし、それぞれのタスクの作業進捗を管理するものです。

タスクの基本的な進行は、「タスク予約」→「編集完了」→「検証(OK)」というフェーズで進めます。

![TaskStatus](https://github.com/yuuhayashi/task-bldg/wiki/TaskStatus)


## 起動方法

- `task-bldg-0.0.0.war` を起動

	```
	java -jar task-bldg-0.0.0.war
	```

- Webブラウザから `http://localhost:8083/task-bldg/` にアクセスする。


## 操作方法

ポータルの利用者のための[操作説明書](https://github.com/yuuhayashi/task-bldg/wiki/HowToUse)


## システム構築

### リバースプロキシの設定

- nginxを利用したリバースプロキシの設定例

`/etc/nginx/conf.d/default.conf`

	```
	server {
    server_name    surveyor.mydns.jp;

    proxy_set_header    Host    $host;
    proxy_set_header    X-Real-IP    $remote_addr;
    proxy_set_header    X-Forwarded-Host       $host;
    proxy_set_header    X-Forwarded-Server    $host;
    proxy_set_header    X-Forwarded-For    $proxy_add_x_forwarded_for;

	  location / {
	        root /var/www/html;
	        index   index.html index.htm;
	  }
	
	  location /task-bldg/ {
	        proxy_pass    http://192.168.0.203:8083/task-bldg/;
	        proxy_redirect  http://192.168.0.203:8083/task-bldg/ http://surveyor.mydns.jp/task-bldg/;
	  }
	:
	```

	