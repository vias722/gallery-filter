<template>
<v-navigation-drawer app clipped dark :value="value" v-on:input="$emit('input', $event)">
    <v-list >
        <v-list-item-group mandatory >
            <v-list-item selectable to="/">
                <v-list-item-icon><v-icon>mdi-information</v-icon></v-list-item-icon>
                <v-list-item-content>
                    <v-list-item-title>はじめに</v-list-item-title>
                </v-list-item-content>
            </v-list-item>
            <v-subheader >
                フィルターを適用する
            </v-subheader>
            <v-divider />
            <v-list-item selectable to="/gallery/timeline" v-if="store.user">
                    <v-list-item-icon><v-icon>mdi-format-list-text</v-icon></v-list-item-icon>
                    <v-list-item-content>
                        <v-list-item-title>タイムライン</v-list-item-title>
                    </v-list-item-content>
            </v-list-item>
            <v-divider />
                <v-divider />
            <v-list-group
                :value="true"
                v-if="store.user"
                color="dark"
            >
                <template v-slot:activator class="grey darken-1">
                    <v-list-item-icon><v-icon>mdi-format-list-bulleted</v-icon></v-list-item-icon>
                    <v-list-item-title >リスト</v-list-item-title>
                </template>
                <v-list-item color="dark" selectable :to="`/gallery/${list.id}`" v-for="list in lists"
                :key="list.id">
                    <v-list-item-icon><v-icon>{{list.mode == "public" ? "mdi-format-list-text" : "mdi-lock"}}</v-icon></v-list-item-icon>
                    <v-list-item-content>
                        <v-list-item-title>{{list.name}}</v-list-item-title>
                    </v-list-item-content>
                </v-list-item>
                <v-divider />
            </v-list-group>
            </v-list-item-group>
        
    </v-list>
</v-navigation-drawer>
</template>

<script>
import Axios from 'axios'
import store from "../plugins/store"

export default {
    props: ['value'],
    data: function (){
        return {
            store: store,
            lists: null
        }
    },

    watch: {
        store: {
            deep: true,
            handler: function(store){
                Axios.get('tweet/list').then((res)=>{
                    this.lists = res.data.filter((value)=>{
                        return store.user.id == value.user.id
                    })
                })
            }
        }
    }
}
</script>
