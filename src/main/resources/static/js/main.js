// 用户状态管理
const Auth = {
    getUser: () => JSON.parse(localStorage.getItem('user')),
    setUser: (user) => localStorage.setItem('user', JSON.stringify(user)),
    logout: () => { localStorage.removeItem('user'); location.href = '/login'; },
    check: () => { if (!Auth.getUser()) location.href = '/login'; }
};

// API请求封装
async function api(url, options = {}) {
    const res = await fetch(url, {
        headers: { 'Content-Type': 'application/json', ...options.headers },
        ...options
    });
    return res.json();
}

// 格式化日期
function formatDate(str) {
    if (!str) return '';
    const d = new Date(str);
    return `${d.getFullYear()}-${String(d.getMonth()+1).padStart(2,'0')}-${String(d.getDate()).padStart(2,'0')} ${String(d.getHours()).padStart(2,'0')}:${String(d.getMinutes()).padStart(2,'0')}`;
}

// 活动类型显示
function getTypeBadge(type) {
    const map = { ONLINE: ['线上', 'badge-online'], OFFLINE: ['线下', 'badge-offline'], HYBRID: ['混合', 'badge-hybrid'] };
    const [text, cls] = map[type] || ['未知', ''];
    return `<span class="badge ${cls}">${text}</span>`;
}

// 签到状态判断
function getCheckInStatus(activity, checkedIn) {
    const now = new Date();
    const start = new Date(activity.startTime);
    const end = new Date(activity.endTime);
    const checkInStart = new Date(start.getTime() - 10 * 60 * 1000);
    if (checkedIn) return { text: '已签到', disabled: true, cls: 'btn-success' };
    if (now < checkInStart) return { text: '签到未开始', disabled: true, cls: 'btn-secondary' };
    if (now > end) return { text: '已结束', disabled: true, cls: 'btn-secondary' };
    return { text: '立即签到', disabled: false, cls: 'btn-primary' };
}

// ==================== 分享功能 ====================

/**
 * 分享活动 - 复制活动链接到剪贴板
 * @param {number} activityId - 活动ID
 * @param {string} activityTitle - 活动标题（可选，用于提示）
 */
function shareActivity(activityId, activityTitle) {
    const url = `${window.location.origin}/activity/${activityId}`;
    copyToClipboard(url, activityTitle ? `活动「${activityTitle}」` : '活动');
}

/**
 * 分享评论/读后感 - 复制包含评论锚点的链接到剪贴板
 * @param {number} activityId - 活动ID
 * @param {number} commentId - 评论ID
 */
function shareComment(activityId, commentId) {
    const url = `${window.location.origin}/activity/${activityId}#comment-${commentId}`;
    copyToClipboard(url, '评论');
}

/**
 * 通用复制到剪贴板函数
 * @param {string} text - 要复制的文本
 * @param {string} itemName - 项目名称（用于提示）
 */
function copyToClipboard(text, itemName = '链接') {
    // 优先使用现代 Clipboard API
    if (navigator.clipboard && navigator.clipboard.writeText) {
        navigator.clipboard.writeText(text).then(() => {
            showShareToast(`${itemName}链接已复制，快去分享给好友吧！`);
        }).catch(err => {
            // 降级到传统方法
            fallbackCopyToClipboard(text, itemName);
        });
    } else {
        // 不支持 Clipboard API，使用传统方法
        fallbackCopyToClipboard(text, itemName);
    }
}

/**
 * 传统复制方法（兼容旧浏览器）
 */
function fallbackCopyToClipboard(text, itemName) {
    const textArea = document.createElement('textarea');
    textArea.value = text;
    textArea.style.position = 'fixed';
    textArea.style.left = '-9999px';
    textArea.style.top = '-9999px';
    document.body.appendChild(textArea);
    textArea.focus();
    textArea.select();

    try {
        const successful = document.execCommand('copy');
        if (successful) {
            showShareToast(`${itemName}链接已复制，快去分享给好友吧！`);
        } else {
            showShareToast('复制失败，请手动复制链接', 'error');
        }
    } catch (err) {
        showShareToast('复制失败，请手动复制链接', 'error');
    }

    document.body.removeChild(textArea);
}

/**
 * 显示分享提示Toast
 * @param {string} message - 提示消息
 * @param {string} type - 类型：success/error
 */
function showShareToast(message, type = 'success') {
    // 移除已存在的toast
    const existingToast = document.querySelector('.share-toast');
    if (existingToast) {
        existingToast.remove();
    }

    const toast = document.createElement('div');
    toast.className = `share-toast share-toast-${type}`;
    toast.innerHTML = `
        <span class="share-toast-icon">${type === 'success' ? '✓' : '✕'}</span>
        <span class="share-toast-message">${message}</span>
    `;
    document.body.appendChild(toast);

    // 动画显示
    setTimeout(() => toast.classList.add('show'), 10);

    // 3秒后自动消失
    setTimeout(() => {
        toast.classList.remove('show');
        setTimeout(() => toast.remove(), 300);
    }, 3000);
}

// 动态添加Toast样式（确保样式存在）
function injectShareStyles() {
    if (document.getElementById('share-styles')) return;

    const style = document.createElement('style');
    style.id = 'share-styles';
    style.textContent = `
        .share-toast {
            position: fixed;
            top: 20px;
            left: 50%;
            transform: translateX(-50%) translateY(-100px);
            background: #1a365d;
            color: white;
            padding: 12px 24px;
            border-radius: 8px;
            box-shadow: 0 4px 12px rgba(0,0,0,0.15);
            z-index: 10000;
            display: flex;
            align-items: center;
            gap: 10px;
            font-size: 14px;
            transition: transform 0.3s ease;
        }
        .share-toast.show {
            transform: translateX(-50%) translateY(0);
        }
        .share-toast-success {
            background: #276749;
        }
        .share-toast-error {
            background: #c53030;
        }
        .share-toast-icon {
            font-size: 18px;
            font-weight: bold;
        }
        .share-btn {
            background: none;
            border: 1px solid var(--gray-200);
            padding: 0.25rem 0.75rem;
            border-radius: 4px;
            cursor: pointer;
            display: inline-flex;
            align-items: center;
            gap: 0.25rem;
            font-size: 0.875rem;
            color: var(--gray-600);
            transition: all 0.2s;
        }
        .share-btn:hover {
            border-color: var(--primary);
            color: var(--primary);
            background: #ebf4ff;
        }
    `;
    document.head.appendChild(style);
}

// 页面加载时注入样式
document.addEventListener('DOMContentLoaded', () => {
    injectShareStyles();
});

// ==================== 分享功能结束 ====================

// 更新导航栏
function updateNavbar() {
    const user = Auth.getUser();
    const navbar = document.querySelector('.navbar');
    if (!navbar) return;
    if (user) {
        // 获取未读消息数
        api(`/api/notification/unread-count?userId=${user.id}`).then(res => {
            const count = res.data || 0;
            const badge = count > 0 ? `<span class="notification-badge">${count}</span>` : '';
            navbar.innerHTML = `
                <a href="/" class="logo">📚 读书会</a>
                <div class="nav-links">
                    <a href="/activities">活动列表</a>
                    <a href="/my-registrations">我的报名</a>
                    <a href="/my-favorites">我的收藏</a>
                    <a href="/my-activities">活动管理</a>
                    <a href="/notifications">消息${badge}</a>
                </div>
                <div class="user-info">
                    <a href="/profile" style="color:white">${user.nickname || user.username}</a>
                    <span style="opacity:0.7">${user.role === 'ADMIN' ? '[管理员]' : ''}</span>
                    <button class="btn btn-sm btn-secondary" onclick="Auth.logout()">退出</button>
                </div>
            `;
        });
    } else {
        navbar.innerHTML = `
            <a href="/" class="logo">📚 读书会</a>
            <div class="nav-links">
                <a href="/activities">活动列表</a>
                <a href="/login">登录</a>
                <a href="/register">注册</a>
            </div>
        `;
    }
}

// 页面加载时更新导航
document.addEventListener('DOMContentLoaded', updateNavbar);

// 每30秒刷新未读消息数
setInterval(() => {
    const user = Auth.getUser();
    if (user) updateNavbar();
}, 30000);