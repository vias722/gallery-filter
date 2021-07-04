<template>
    <v-dialog v-model="value" height="100vh">
        <template #activator="{on, attrs}" width="300px">
            <v-img
                alt=""
                :src="media.media_url"
                height=200px
                v-bind="attrs"
                v-on="on"
            ></v-img>
        </template>
            <!-- <v-card width="300px">test</v-card> -->
            <v-img
                alt=""
                :src="`${media.media_url}`"
                height="90vh"
                contain
                @click="value=false"
            ></v-img>
    </v-dialog>
    
</template>

<script>
export default {
    props: ['media'],
    data: function () {
        return {
            value: false
        }
    }
}
</script>
