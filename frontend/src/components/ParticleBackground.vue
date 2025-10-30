<template>
  <div ref="particleCanvas" class="particle-background"></div>
</template>

<script>
import { ref, onMounted, onUnmounted } from 'vue'

export default {
  name: 'ParticleBackground',
  setup() {
    const particleCanvas = ref(null)
    let canvas, ctx, particles, animationId

    class Particle {
      constructor(canvasWidth, canvasHeight) {
        this.x = Math.random() * canvasWidth
        this.y = Math.random() * canvasHeight
        this.vx = (Math.random() - 0.5) * 0.5
        this.vy = (Math.random() - 0.5) * 0.5
        this.radius = Math.random() * 2 + 1
        this.opacity = Math.random() * 0.5 + 0.2
      }

      update(canvasWidth, canvasHeight) {
        this.x += this.vx
        this.y += this.vy

        if (this.x < 0 || this.x > canvasWidth) this.vx *= -1
        if (this.y < 0 || this.y > canvasHeight) this.vy *= -1
      }

      draw(ctx) {
        ctx.beginPath()
        ctx.arc(this.x, this.y, this.radius, 0, Math.PI * 2)
        ctx.fillStyle = `rgba(102, 126, 234, ${this.opacity})`
        ctx.fill()
      }
    }

    const initParticles = () => {
      canvas = document.createElement('canvas')
      ctx = canvas.getContext('2d')
      
      canvas.width = particleCanvas.value.offsetWidth
      canvas.height = particleCanvas.value.offsetHeight
      
      particleCanvas.value.appendChild(canvas)

      particles = []
      const particleCount = Math.floor((canvas.width * canvas.height) / 10000)
      
      for (let i = 0; i < particleCount; i++) {
        particles.push(new Particle(canvas.width, canvas.height))
      }

      animate()
    }

    const animate = () => {
      ctx.clearRect(0, 0, canvas.width, canvas.height)

      particles.forEach(particle => {
        particle.update(canvas.width, canvas.height)
        particle.draw(ctx)
      })

      // Draw connections
      particles.forEach((p1, i) => {
        particles.slice(i + 1).forEach(p2 => {
          const dx = p1.x - p2.x
          const dy = p1.y - p2.y
          const distance = Math.sqrt(dx * dx + dy * dy)

          if (distance < 120) {
            ctx.beginPath()
            ctx.strokeStyle = `rgba(102, 126, 234, ${0.15 * (1 - distance / 120)})`
            ctx.lineWidth = 1
            ctx.moveTo(p1.x, p1.y)
            ctx.lineTo(p2.x, p2.y)
            ctx.stroke()
          }
        })
      })

      animationId = requestAnimationFrame(animate)
    }

    const handleResize = () => {
      if (canvas && particleCanvas.value) {
        canvas.width = particleCanvas.value.offsetWidth
        canvas.height = particleCanvas.value.offsetHeight
      }
    }

    onMounted(() => {
      initParticles()
      window.addEventListener('resize', handleResize)
    })

    onUnmounted(() => {
      if (animationId) {
        cancelAnimationFrame(animationId)
      }
      window.removeEventListener('resize', handleResize)
    })

    return {
      particleCanvas
    }
  }
}
</script>

<style scoped>
.particle-background {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  overflow: hidden;
  z-index: 0;
}

.particle-background canvas {
  display: block;
  width: 100%;
  height: 100%;
}
</style>
