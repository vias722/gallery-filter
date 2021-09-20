<template>
  <v-container height="auto">
    <v-tabs grow v-model="filterType">
        <v-tab>イラスト</v-tab>
        <v-tab>写真</v-tab>
    </v-tabs>
    <v-divider />
    <v-virtual-scroll
        v-if="!onRefresh"
        height="auto"
        itemHeight="400"
        :items="filterType== 0 ? tweets2d : tweets3d" :key="filterType" >
        <template #default="{item}">
            <v-card :key="item.id">
                <v-container fluid>
                    <v-row>
                        <v-col cols=3
                            v-for="media in item.extended_entities.media"
                            :key="media.id">
                            <v-card >
                                <ImageCard :media="media" />
                            </v-card>
                        </v-col>
                    </v-row>
                </v-container>
                <v-card-text>
                    {{getOriginal(item).text}}
                </v-card-text>
                <v-card-actions>
                    <v-list-item class="grow">
                        <v-list-item-avatar color="grey darken-3">
                        <v-img
                            class="elevation-6"
                            alt=""
                            :src="getOriginal(item).user.profile_image_url"
                        ></v-img>
                        </v-list-item-avatar>

                        <v-list-item-content>
                        <v-list-item-title>{{getOriginal(item).user.name}}(@{{getOriginal(item).user.screen_name}})</v-list-item-title>
                        </v-list-item-content>
                        <v-spacer/>
                        <v-row
                        align="center"
                        justify="end"
                        >
                        <v-icon class="mr-1">
                            mdi-twitter-retweet
                        </v-icon>
                        <span class="subheading">{{getOriginal(item).retweet_count}}</span>
                        <v-icon class="mr-1">
                            mdi-heart
                        </v-icon>
                        <span class="subheading mr-2">{{getOriginal(item).favorite_count}}</span>
                        <span class="mr-1">·</span>
                        
                        </v-row>
                    </v-list-item>
                    </v-card-actions>
            </v-card>
        </template>
    </v-virtual-scroll>
    <div width="100%" v-else style="text-align: center">
        <v-progress-circular
            class="mt-12"
            size=64
            indeterminate
            color="primary"
        ></v-progress-circular>
    </div>
    
  </v-container>
</template>
<script>
import Axios from 'axios'
import store from '../plugins/store'
import ImageCard from '../components/ImageCard.vue'

export default {
    props: ['id'],
    components: {
        ImageCard
    },
    data: function(){
        return {
            filterType: 0,
            tweets2d: [],
            tweets3d: [],
            onRefresh: false,
            store: store
        }
    },
    watch: {
        id: function () {
            this.refresh()
        },
        store: {
            deep: true,
            handler: function(store){
                if(store.user===null) this.$router.push('/')

                this.refresh()
            }
        }
    },
    mounted: function () {
        this.refresh()
    },
    methods: {
        refresh: function() {
            if(this.store.user === null) this.$router.push('/')
            else if (this.store.user === undefined) return
            this.onRefresh = true
            const url = this.id == "timeline" ? `tweet` : `tweet/list/${this.id}`
            const id = this.id
            this.tweets2d = []
            this.tweets3d = []
            Axios.get(url).then((res)=>{
                if (this.id != id) return
                this.tweets2d = res.data.tweetsWith2D
                this.tweets3d = res.data.tweetsWith3D
                this.onRefresh = false
            })
        },

        getOriginal: function(tweet){
            return tweet.retweeted_status ? tweet.retweeted_status : tweet
        }
    }
}
</script>
