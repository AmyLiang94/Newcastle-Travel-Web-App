<template>
  <el-dialog
    :title="!dataForm.attractionId ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="" prop="attractionName">
      <el-input v-model="dataForm.attractionName" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="description">
      <el-input v-model="dataForm.description" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="category">
      <el-input v-model="dataForm.category" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="latitude">
      <el-input v-model="dataForm.latitude" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="longitude">
      <el-input v-model="dataForm.longitude" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="openingHours">
      <el-input v-model="dataForm.openingHours" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="ticketPrice">
      <el-input v-model="dataForm.ticketPrice" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="imageUrl">
      <el-input v-model="dataForm.imageUrl" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="attrRating">
      <el-input v-model="dataForm.attrRating" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="wheelchairAllow">
      <el-input v-model="dataForm.wheelchairAllow" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="pramAllow">
      <el-input v-model="dataForm.pramAllow" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="hearingAllow">
      <el-input v-model="dataForm.hearingAllow" placeholder=""></el-input>
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
          attractionId: 0,
          attractionName: '',
          description: '',
          category: '',
          latitude: '',
          longitude: '',
          openingHours: '',
          ticketPrice: '',
          imageUrl: '',
          attrRating: '',
          wheelchairAllow: '',
          pramAllow: '',
          hearingAllow: ''
        },
        dataRule: {
          attractionName: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          description: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          category: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          latitude: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          longitude: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          openingHours: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          ticketPrice: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          imageUrl: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          attrRating: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          wheelchairAllow: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          pramAllow: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          hearingAllow: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      init (id) {
        this.dataForm.attractionId = id || 0
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.attractionId) {
            this.$http({
              url: this.$http.adornUrl(`/product/attractions/info/${this.dataForm.attractionId}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.attractionName = data.attractions.attractionName
                this.dataForm.description = data.attractions.description
                this.dataForm.category = data.attractions.category
                this.dataForm.latitude = data.attractions.latitude
                this.dataForm.longitude = data.attractions.longitude
                this.dataForm.openingHours = data.attractions.openingHours
                this.dataForm.ticketPrice = data.attractions.ticketPrice
                this.dataForm.imageUrl = data.attractions.imageUrl
                this.dataForm.attrRating = data.attractions.attrRating
                this.dataForm.wheelchairAllow = data.attractions.wheelchairAllow
                this.dataForm.pramAllow = data.attractions.pramAllow
                this.dataForm.hearingAllow = data.attractions.hearingAllow
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
              url: this.$http.adornUrl(`/product/attractions/${!this.dataForm.attractionId ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'attractionId': this.dataForm.attractionId || undefined,
                'attractionName': this.dataForm.attractionName,
                'description': this.dataForm.description,
                'category': this.dataForm.category,
                'latitude': this.dataForm.latitude,
                'longitude': this.dataForm.longitude,
                'openingHours': this.dataForm.openingHours,
                'ticketPrice': this.dataForm.ticketPrice,
                'imageUrl': this.dataForm.imageUrl,
                'attrRating': this.dataForm.attrRating,
                'wheelchairAllow': this.dataForm.wheelchairAllow,
                'pramAllow': this.dataForm.pramAllow,
                'hearingAllow': this.dataForm.hearingAllow
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
