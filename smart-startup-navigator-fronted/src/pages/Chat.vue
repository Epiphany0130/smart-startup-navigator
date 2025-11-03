<template>
  <div class="chat-wrap">
    <header class="chat-header">
      <h3>{{ title }}</h3>
      <small class="stage">阶段标识：{{ stage }}</small>
    </header>
    <section class="messages" ref="messagesEl">
      <div v-for="(m, i) in messages" :key="i" :class="['msg', m.role]">
        <div class="bubble">
          <div class="md" v-html="render(m.content)"></div>
        </div>
      </div>
      <div v-if="loading" class="msg assistant loading">
        <div class="bubble">正在思考...</div>
      </div>
    </section>
    <footer class="composer" @keydown.enter.exact.prevent="send">
      <textarea v-model="input" rows="3" placeholder="输入你的问题，回车发送"></textarea>
      <button :disabled="!canSend" @click="send">发送</button>
    </footer>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, nextTick } from 'vue';
import { useRoute } from 'vue-router';
import { getSseUrl, sendSync } from '../services/api.js';
import MarkdownIt from 'markdown-it';

const route = useRoute();
const stage = computed(() => route?.props?.stage || route?.params?.stage || route?.query?.stage || 'creative');
const title = computed(() => route.meta?.title || '对话');

const messages = ref([{ role: 'assistant', content: `你好！这是 ${title.value} 的智能助手，有什么我可以帮你？` }]);
const input = ref('');
const loading = ref(false);
const eventSourceRef = ref(null);
const chatIdRef = ref(`${stage.value}-${Date.now()}`);

const canSend = computed(() => input.value.trim().length > 0 && !loading.value);
const messagesEl = ref(null);

const md = new MarkdownIt({ html: false, linkify: true, breaks: true });
const render = (text) => md.render(text || '');

function scrollToBottom() {
  nextTick(() => {
    const el = messagesEl.value; if (!el) return; el.scrollTop = el.scrollHeight;
  });
}

function closeSse() {
  if (eventSourceRef.value) {
    try { eventSourceRef.value.close(); } catch {}
    eventSourceRef.value = null;
  }
}

async function send() {
  if (!canSend.value) return;
  const userText = input.value.trim();
  input.value = '';
  messages.value.push({ role: 'user', content: userText });
  scrollToBottom();
  loading.value = true;

  // 优先使用后端提供的 SSE 接口（分阶段）
  const sseUrl = getSseUrl(stage.value, userText, chatIdRef.value);
  if (sseUrl) {
    try {
      closeSse();
      const es = new EventSource(sseUrl);
      eventSourceRef.value = es;
      let acc = '';
      es.onmessage = (ev) => {
        const chunk = ev?.data ?? '';
        acc += chunk;
        // 实时更新最后一条 assistant 消息
        const last = messages.value[messages.value.length - 1];
        if (last?.role === 'assistant' && last._streaming) {
          last.content = acc;
        } else {
          messages.value.push({ role: 'assistant', content: chunk, _streaming: true });
        }
        scrollToBottom();
      };
      es.onerror = async (err) => {
        // 不展示错误提示，静默回退到同步接口
        const hadContent = acc && acc.trim().length > 0;
        closeSse();
        try {
          if (!hadContent) {
            const data = await sendSync(userText, chatIdRef.value);
            const reply = typeof data === 'string' ? data : (data?.reply ?? data?.data ?? JSON.stringify(data));
            messages.value.push({ role: 'assistant', content: reply });
          }
        } catch (_) {
          // 同步也失败则保持沉默，不再插入“失败”提示
        } finally {
          loading.value = false;
          scrollToBottom();
        }
      };
      es.onopen = () => { loading.value = false; /* 连接打开后取消加载状态 */ };
      // 服务端完成后通常会关闭连接；在关闭时取消 _streaming 标记
      es.addEventListener('end', () => {
        const last = messages.value[messages.value.length - 1];
        if (last) delete last._streaming;
        closeSse();
        loading.value = false;
      });
    } catch (e) {
      closeSse();
      loading.value = false;
      messages.value.push({ role: 'assistant', content: `连接 SSE 出错：${e?.message || e}` });
    }
    return;
  }

  // 回退到同步接口（总服务 TOTAL）
  try {
    const data = await sendSync(userText, chatIdRef.value);
    const reply = typeof data === 'string' ? data : (data?.reply ?? data?.data ?? JSON.stringify(data));
    messages.value.push({ role: 'assistant', content: reply });
  } catch (e) {
    messages.value.push({ role: 'assistant', content: `调用后端接口失败：${e?.message || e}` });
  } finally {
    loading.value = false;
    scrollToBottom();
  }
}

onMounted(scrollToBottom);
</script>

<style scoped>
.chat-wrap { max-width: 960px; margin: 0 auto; padding: 20px; }
.chat-header { display: flex; align-items: baseline; gap: 12px; margin-bottom: 12px; }
.stage { opacity: 0.7; }
.messages { background: var(--panel); border: 1px solid #1f2937; border-radius: 12px; min-height: 50vh; padding: 16px; overflow: auto; display: flex; flex-direction: column; gap: 12px; }
.msg { margin-bottom: 12px; display: flex; }
.msg.user { justify-content: flex-end; }
.bubble { max-width: 75%; background: #0b1220; border: 1px solid #1f2937; padding: 10px 12px; border-radius: 12px; overflow-wrap: anywhere; word-break: break-word; }
.bubble .md { line-height: 1.6; }
.msg.user .bubble { background: #0e1a29; border-color: #1b2b3b; }
.composer { display: grid; grid-template-columns: 1fr auto; gap: 8px; margin-top: 12px; }
textarea { width: 100%; resize: vertical; border-radius: 8px; border: 1px solid #243040; background: #0b1220; color: var(--text); padding: 8px; }
button { background: var(--accent); color: #0b1c12; border: none; padding: 10px 16px; border-radius: 8px; cursor: pointer; }
button[disabled] { opacity: 0.6; cursor: not-allowed; }
</style>