# Gallery-Filter

Gallery-Filterはタイムライン/リストからイラストツイートのみ/写真ツイートのみを抽出できるウェブアプリです。
実行にはTwitterのAPIKey / APISecretが必要です。
(プロジェクト内には記載がありません)

## 必要環境
* jdk 11
* yarn (npmも可？)
* python 3.9
* poetry
* docker, docker-compose

## 環境変数について
TWITTER_API_KEY / TWITTER_API_SECRET それぞれをTwitterから取得した値で埋めてください。
localからの実行及びコンテナのビルドでその値を参照しています。

## プロジェクト構成
### gallery-filter-server
SpringBootによるフロント側のWebアプリです。
静的ファイルとしてgallery-filter-clientのVue.jsのフロント画面を含んでおり、そのフロント画面と通信するAPIを有しています。

起動時のポートは標準の8080です。

### gallery-filter-client
Vue.jsによるGallery-Filterのフロント画面です。
起動時のポートは8081であり、gallery-filter-serverが8080で起動している場合、`yarn serve` で起動するとプロキシ機能によってそちらと連携することが出来ます。

yarn buildを実施すると、gallery-filter-serverの/src/resources/static配下に成果物が配置されます。

### gallery-filter-engine
flaskによるGallery-Filterの画像判別APIです。
pytorchによって学習させたAIのモデルを含んでいます。

なお、モデルの学習データとしては、イラストはPixivの東方タグのもの、写真はflickrのjapanタグのものを用いました。

## ビルド方法
各プロジェクトの階層に移動し以下のコマンドを実行した後、最後に記述してある手順を実施してください。
### gallery-filter-client
```
$ yarn build
```
### gallery-filter-server
```
$ ./gradlew build
```
※ gallery-filter-clientを先に実行してください。

### gallery-filter-engine
特に必要ありません。

### 最後に
このreadme.mdと同階層で以下のコマンドを実行してください。
```
$ docker-compose build
```
その後以下のコマンドでローカルで実行されます。
```
$ docker-compose up
```