<template>
  <div class="ai-assistant">
    <div class="header-section">
      <h2>智能职业助手</h2>
      <p>基于AI技术为您提供个性化的职业发展建议</p>
    </div>
    
    <div class="assistant-content">
      <div class="chat-container">
        <div class="chat-messages" ref="chatContainer">
          <div 
            v-for="message in messages" 
            :key="message.id" 
            :class="['message', message.role]"
          >
            <div class="message-content">
              <div class="avatar">
                <el-icon v-if="message.role === 'user'"><User /></el-icon>
                <el-icon v-else><Lightning /></el-icon>
              </div>
              <div class="text">
                <div class="name">{{ message.role === 'user' ? '我' : '职业助手' }}</div>
                <div class="content">{{ message.content }}</div>
              </div>
            </div>
          </div>
        </div>
        
        <div class="chat-input">
          <el-input
            v-model="inputMessage"
            type="textarea"
            :rows="3"
            placeholder="请输入您的问题，例如：我应该如何提升技术能力？"
            @keyup.enter="sendMessage"
          ></el-input>
          <el-button 
            type="primary" 
            @click="sendMessage" 
            :loading="loading"
            :disabled="!inputMessage.trim()"
          >
            发送
          </el-button>
        </div>
      </div>
      
      <div class="suggestions">
        <h3>推荐问题</h3>
        <div class="suggestion-list">
          <div 
            class="suggestion-item" 
            v-for="suggestion in suggestions" 
            :key="suggestion"
            @click="selectSuggestion(suggestion)"
          >
            {{ suggestion }}
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted, nextTick } from 'vue'
import { sendMessageToAI } from '@/api/ai'
import { ElMessage } from 'element-plus'
import { User, Lightning } from '@element-plus/icons-vue'

export default {
  name: 'AIAssistant',
  components: {
    User,
    Lightning
  },
  setup() {
    const messages = ref([
      {
        id: 1,
        role: 'assistant',
        content: '您好！我是您的智能职业助手。我可以根据您的职业历程和技能情况，为您提供个性化的职业发展建议。请问有什么我可以帮助您的吗？'
      }
    ])
    const inputMessage = ref('')
    const loading = ref(false)
    const chatContainer = ref(null)
    const currentUser = ref(null)
    
    const suggestions = ref([
      '我应该如何制定职业发展计划？',
      '如何提升我的技术能力？',
      '我适合申请哪个职位的晋升？',
      '如何平衡工作与学习？',
      '我应该学习哪些新技能？'
    ])
    
    const scrollToBottom = () => {
      nextTick(() => {
        if (chatContainer.value) {
          chatContainer.value.scrollTop = chatContainer.value.scrollHeight
        }
      })
    }
    
    const sendMessage = async () => {
      if (!inputMessage.value.trim() || loading.value) return
      
      // 添加用户消息
      const userMessage = {
        id: Date.now(),
        role: 'user',
        content: inputMessage.value
      }
      messages.value.push(userMessage)
      
      // 清空输入框
      const userQuestion = inputMessage.value
      inputMessage.value = ''
      
      // 滚动到底部
      scrollToBottom()
      
      try {
        loading.value = true
        
        // 调用AI接口
        const response = await sendMessageToAI({
          message: userQuestion,
          employeeId: currentUser.value ? currentUser.value.id : null
        })
        
        // 添加AI回复
        const aiMessage = {
          id: Date.now() + 1,
          role: 'assistant',
          content: response.data.reply || '抱歉，我没有理解您的问题。请重新表述一下。'
        }
        messages.value.push(aiMessage)
        
        // 滚动到底部
        scrollToBottom()
      } catch (error) {
        ElMessage.error('发送消息失败')
        // 添加错误回复
        const errorMessage = {
          id: Date.now() + 1,
          role: 'assistant',
          content: '抱歉，网络出现了一些问题，请稍后再试。'
        }
        messages.value.push(errorMessage)
        scrollToBottom()
      } finally {
        loading.value = false
      }
    }
    
    const selectSuggestion = (suggestion) => {
      inputMessage.value = suggestion
      sendMessage()
    }
    
    onMounted(() => {
      // Get current user from local storage
      const user = localStorage.getItem('currentUser')
      if (user) {
        currentUser.value = JSON.parse(user)
      }
      
      scrollToBottom()
    })
    
    return {
      messages,
      inputMessage,
      loading,
      chatContainer,
      suggestions,
      sendMessage,
      selectSuggestion,
      scrollToBottom
    }
  }
}
</script>

<style scoped>
.ai-assistant {
  padding: 20px;
  height: calc(100vh - 120px);
  display: flex;
  flex-direction: column;
}

.header-section {
  text-align: center;
  margin-bottom: 30px;
}

.header-section h2 {
  font-size: 28px;
  color: #333;
  margin-bottom: 10px;
}

.header-section p {
  font-size: 16px;
  color: #666;
}

.assistant-content {
  display: flex;
  flex: 1;
  gap: 20px;
}

.chat-container {
  flex: 1;
  display: flex;
  flex-direction: column;
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  overflow: hidden;
}

.chat-messages {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.message {
  display: flex;
}

.message.user {
  justify-content: flex-end;
}

.message-content {
  display: flex;
  gap: 10px;
  max-width: 80%;
}

.message.user .message-content {
  flex-direction: row-reverse;
}

.avatar {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  background: #ff6b35;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  flex-shrink: 0;
}

.message.assistant .avatar {
  background: #409eff;
}

.text {
  display: flex;
  flex-direction: column;
}

.name {
  font-size: 12px;
  color: #999;
  margin-bottom: 4px;
}

.content {
  background: #f0f2f5;
  padding: 12px 16px;
  border-radius: 18px;
  color: #333;
  line-height: 1.5;
}

.message.user .content {
  background: #ff6b35;
  color: white;
  border-bottom-right-radius: 4px;
}

.message.assistant .content {
  background: #f0f2f5;
  border-bottom-left-radius: 4px;
}

.chat-input {
  padding: 20px;
  border-top: 1px solid #eee;
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.chat-input .el-textarea {
  flex: 1;
}

.chat-input .el-button {
  align-self: flex-end;
}

.suggestions {
  width: 300px;
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  padding: 20px;
}

.suggestions h3 {
  margin-top: 0;
  margin-bottom: 15px;
  color: #333;
}

.suggestion-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.suggestion-item {
  padding: 12px 16px;
  background: #f0f2f5;
  border-radius: 8px;
  cursor: pointer;
  font-size: 14px;
  color: #666;
  transition: all 0.3s;
}

.suggestion-item:hover {
  background: #e1e5ea;
  color: #333;
}
</style>