<template>
  <v-container>
    <v-row class="text-center">

      <v-col class="mb-4">
        <h1 class="display-2 font-weight-bold mb-3">Gallery Filter</h1>

        <p class="subheading font-weight-regular">
          タイムライン / リストからイラスト/写真を抽出します。
        </p>
      </v-col>

      <v-col class="mb-5" cols="12">
        <h2 class="headline font-weight-bold mb-5">注意事項</h2>

        <v-row justify="center">
          <div style="text-align: start">
              <ul>
                  <li>表示には時間がかかります。(10秒程度)</li>
                  <li>現在は各タイムライン/リストの最新200ツイート(のうち画像つきのツイート)のみが対象です。</li>
                  <li><b>一つでもイラストがあれば</b>イラストツイートに分類されます。</li>
                  <li>完全なイラストや写真は高確率で正しく分類出来ますが、ゲームのスクリーンショットやその他の図表の分類先はまちまちです。</li>
              </ul>
          </div>
        </v-row>
      </v-col>

      <v-col class="mb-5" cols="12">
        <h2 class="headline font-weight-bold mb-5">今後の改善事項(すぐ実装方針が定まるもの)</h2>

        <v-row justify="center">
          <div style="text-align: start">
              <ul>
                  <li>判定結果のキャッシュ(現在はアクセスごとに全画像を再判定)</li>
                  <li>最新200ツイート以降の取得</li>
                  <li>誤判定の結果を集計し(報告ボタン等を設置)、定期的に再学習する仕組み作り</li>
                  <li>細かいエラーハンドリングの実装</li>
              </ul>
          </div>
        </v-row>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
export default {
  name: "Introduction",

};
</script>
