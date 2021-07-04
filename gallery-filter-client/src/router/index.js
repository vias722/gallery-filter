import Vue from "vue";
import VueRouter from "vue-router";
import Home from "../views/Home.vue";
import Gallery from "../views/Gallery.vue"

Vue.use(VueRouter);

const routes = [
  {
    path: "/",
    name: "Home",
    component: Home,
  },
  {
    path: "/gallery/:id",
    name: "Gallery",
    component: Gallery,
    props: true
  },
];

const router = new VueRouter({
  routes,
});

export default router;
