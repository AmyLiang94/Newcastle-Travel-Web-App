<template>
  <el-dialog
    :title="!dataForm.reviewId ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="" prop="attractionId">
      <el-input v-model="dataForm.attractionId" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="userId">
      <el-input v-model="dataForm.userId" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="rating">
      <el-input v-model="dataForm.rating" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="reviewText">
      <el-input v-model="dataForm.reviewText" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="createTime">
      <el-input v-model="dataForm.createTime" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="updateTime">
      <el-input v-model="dataForm.updateTime" placeholder=""></el-input>
    </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
    </span>
  </el-dialog>
</template>

<script>
  export default {
    data () {
      return {
        visible: false,
        dataForm: {
          reviewId: 0,
          attractionId: '',
          userId: '',
          rating: '',
          reviewText: '',
          createTime: '',
          updateTime: ''
        },
        dataRule: {
          attractionId: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          userId: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          rating: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          reviewText: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          createTime: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          updateTime: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      init (id) {
        this.dataForm.reviewId = id || 0
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.reviewId) {
            this.$http({
              url: this.$http.adornUrl(`/product/reviews/info/${this.dataForm.reviewId}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.attractionId = data.reviews.attractionId
                this.dataForm.userId = data.reviews.userId
                this.dataForm.rating = data.reviews.rating
                this.dataForm.reviewText = data.reviews.reviewText
                this.dataForm.createTime = data.reviews.createTime
                this.dataForm.updateTime = data.reviews.updateTime
              }
            })
          }
        })
      },
      // 表单提交
      dataFormSubmit () {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            this.$http({
              url: this.$http.adornUrl(`/product/reviews/${!this.dataForm.reviewId ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'reviewId': this.dataForm.reviewId || undefined,
                'attractionId': this.dataForm.attractionId,
                'userId': this.dataForm.userId,
                'rating': this.dataForm.rating,
                'reviewText': this.dataForm.reviewText,
                'createTime': this.dataForm.createTime,
                'updateTime': this.dataForm.updateTime
              })
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.$message({
                  message: '操作成功',
                  type: 'success',
                  duration: 1500,
                  onClose: () => {
                    this.visible = false
                    this.$emit('refreshDataList')
                  }
                })
              } else {
                this.$message.error(data.msg)
              }
            })
          }
        })
      }
    }
  }
</script>
