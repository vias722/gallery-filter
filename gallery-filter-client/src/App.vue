<template>
  <v-app>
    <v-app-bar app color="primary" dark clipped-left>
      <v-toolbar-title>Gallery-Filter</v-toolbar-title>
      <v-spacer />
      <TwitterLogin />
    </v-app-bar >
    <ContentDrawer />
    <v-main>
      <router-view />
    </v-main>
  </v-app>
</template>

<script>
import ContentDrawer from './components/ContentDrawer.vue'
import TwitterLogin from './components/TwitterLogin.vue';

export default {
  name: "App",
  components: {
      ContentDrawer,
      TwitterLogin
  },
};
</script>
