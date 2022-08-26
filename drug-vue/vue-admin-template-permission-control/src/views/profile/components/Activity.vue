<template>
  <div class="user-activity">
    <pan-thumb :image="image" />

    <el-button
      type="primary"
      icon="el-icon-upload"
      style="position: absolute; bottom: 15px; margin-left: 40px"
      @click="imagecropperShow = true"
    >
      上传
    </el-button>

    <image-cropper
      v-show="imagecropperShow"
      :key="imagecropperKey"
      :width="300"
      :height="300"
      :url="url"
      lang-type="zh"
      img-format="png"
      :params="params"
      @close="close"
      @crop-upload-success="cropSuccess"
    />
  </div>
</template>

<script>
import ImageCropper from "@/components/ImageCropper";
import PanThumb from "@/components/PanThumb";

export default {
  name: "AvatarUploadDemo",
  components: { ImageCropper, PanThumb },
  data() {
    return {
      url: "",
      imagecropperShow: false,
      imagecropperKey: 0,
      image: "",
      params: {
        userid: this.$store.getters.user.userid,
      },
    };
  },
  mounted() {
    // this.userid = this.$store.getters.user.userid;
    this.url = "http://localhost:8082/upload/file";
  },
  created() {
    this.image = this.$store.getters.avatar;
  },
  methods: {
    cropSuccess(resData) {
      this.imagecropperShow = false;
      this.image = resData.avatar;
      this.$store.getters.user.avatar = resData.avatar;
      this.imagecropperKey = this.imagecropperKey + 1;
      this.$store.dispatch("user/getInfo");
    },
    close() {
      this.imagecropperShow = false;
    },
  },
};
</script>

<style scoped>
.avatar {
  width: 200px;
  height: 200px;
  border-radius: 50%;
}
</style>
