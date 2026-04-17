import { useEffect, useState, useCallback } from 'react';
import { ChevronLeft, ChevronRight } from 'lucide-react';

const SLIDES = [
  '/images/carrossel1.jpeg',
  '/images/carrossel2.jpeg',
  '/images/carrossel3.jpeg',
  '/images/carrossel4.jpg',
  '/images/carrossel5.jpg',
];

const AUTO_MS = 3500;

export default function Carousel() {
  const [current, setCurrent] = useState(0);
  const [paused,  setPaused]  = useState(false);

  const prev = useCallback(() => setCurrent(i => (i - 1 + SLIDES.length) % SLIDES.length), []);
  const next = useCallback(() => setCurrent(i => (i + 1) % SLIDES.length), []);

  useEffect(() => {
    if (paused) return;
    const t = setInterval(next, AUTO_MS);
    return () => clearInterval(t);
  }, [paused, next]);

  return (
    <div style={{ maxWidth: 1280, margin: '0 auto', padding: '1.5rem 1.5rem 0' }}>
      <div
        style={{
          position: 'relative', overflow: 'hidden',
          borderRadius: 20,
          background: '#0f172a',
          userSelect: 'none',
          boxShadow: '0 8px 32px rgba(0,0,0,.15)',
        }}
        onMouseEnter={() => setPaused(true)}
        onMouseLeave={() => setPaused(false)}
      >
        {/* Track */}
        <div style={{
          display: 'flex',
          transform: `translateX(-${current * 100}%)`,
          transition: 'transform .55s cubic-bezier(.77,0,.18,1)',
          willChange: 'transform',
        }}>
          {SLIDES.map((src, i) => (
            <img
              key={src}
              src={src}
              alt={`Slide ${i + 1}`}
              style={{
                flexShrink: 0,
                width: '100%',
                height: 'clamp(160px, 28vw, 380px)',
                objectFit: 'contain',      /* sem corte */
                objectPosition: 'center',
                display: 'block',
                background: '#0f172a',
              }}
            />
          ))}
        </div>

        {/* Setas */}
        <button onClick={prev} style={arrowStyle('left')}  aria-label="Anterior"><ChevronLeft  size={24} color="#fff" strokeWidth={2.5} /></button>
        <button onClick={next} style={arrowStyle('right')} aria-label="Próximo"><ChevronRight size={24} color="#fff" strokeWidth={2.5} /></button>

        {/* Dots */}
        <div style={{ position: 'absolute', bottom: 12, left: '50%', transform: 'translateX(-50%)', display: 'flex', gap: 7 }}>
          {SLIDES.map((_, i) => (
            <button key={i} onClick={() => setCurrent(i)} aria-label={`Slide ${i + 1}`}
              style={{
                width: i === current ? 26 : 7, height: 7,
                borderRadius: 999, border: 'none', padding: 0, cursor: 'pointer',
                background: i === current ? '#fff' : 'rgba(255,255,255,.4)',
                transition: 'width .35s, background .2s',
              }} />
          ))}
        </div>
      </div>
    </div>
  );
}

function arrowStyle(side) {
  return {
    position: 'absolute', top: '50%', transform: 'translateY(-50%)',
    [side]: 14, width: 40, height: 40, borderRadius: '50%',
    background: 'rgba(0,0,0,.45)', border: '1.5px solid rgba(255,255,255,.2)',
    display: 'flex', alignItems: 'center', justifyContent: 'center',
    cursor: 'pointer', backdropFilter: 'blur(4px)', zIndex: 10,
  };
}
