<template>
  <div >
    <a href="auth" v-if="!store.user"><img src="../assets/twitter_signin.png"></a>
    <span v-else > 
        <img :src="store.user.profile_image_url" align="middle" > {{store.user.name}} (@{{store.user.username}}) 
        <v-btn
            href="auth/logout" 
            color="error"
        >ログアウト</v-btn>
        </span>
  </div>
</template>

<script>
import Axios from "axios"
import store from "../plugins/store"

export default {
    data: function (){
        return {
            store: store
        }
    },    
    mounted: function() {
        Axios.get("auth/user")
            .then((res)=>{
                store.user = res.data
            }).catch(()=>{
                store.user = null
            })
    }
}
</script>