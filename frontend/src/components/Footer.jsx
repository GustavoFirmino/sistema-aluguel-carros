import { MapPin, Phone, Mail, Clock, Share2, Globe } from 'lucide-react';

export default function Footer() {
  return (
    <footer style={{ background: '#0f172a', color: '#cbd5e1', fontFamily: 'Inter, system-ui, sans-serif' }}>

      {/* Faixa principal */}
      <div style={{ maxWidth: 1280, margin: '0 auto', padding: '3rem 1.5rem 2rem', display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(220px, 1fr))', gap: '2.5rem' }}>

        {/* Logo + descrição */}
        <div>
          <img src="/images/logo.jpeg" alt="Roda Mundo Aluguel" style={{ height: 72, width: 'auto', objectFit: 'contain', borderRadius: 8, marginBottom: '1rem' }} />
          <p style={{ fontSize: '.85rem', lineHeight: 1.7, color: '#94a3b8', maxWidth: 260 }}>
            Aluguel de veículos com transparência, agilidade e os melhores preços do mercado. Reserve online em minutos.
          </p>
          <div style={{ display: 'flex', gap: '.75rem', marginTop: '1.25rem' }}>
            <SocialBtn href="#" label="Instagram"><Share2 size={16} /></SocialBtn>
            <SocialBtn href="#" label="Facebook"><Globe size={16} /></SocialBtn>
          </div>
        </div>

        {/* Links rápidos */}
        <div>
          <h4 style={heading}>Links rápidos</h4>
          <ul style={{ listStyle: 'none', display: 'flex', flexDirection: 'column', gap: '.55rem' }}>
            {[
              { href: '/',          label: 'Ver veículos disponíveis' },
              { href: '/cadastro',  label: 'Criar conta' },
              { href: '/login',     label: 'Entrar' },
              { href: '/meus-pedidos', label: 'Meus pedidos' },
              { href: '/estrategia',  label: 'Estratégia & Paleta de Cores' },
            ].map(({ href, label }) => (
              <li key={href}>
                <a href={href} style={{ fontSize: '.855rem', color: '#94a3b8', textDecoration: 'none', transition: 'color .15s' }}
                  onMouseEnter={e => e.target.style.color = '#fff'}
                  onMouseLeave={e => e.target.style.color = '#94a3b8'}>
                  → {label}
                </a>
              </li>
            ))}
          </ul>
        </div>

        {/* Contato */}
        <div>
          <h4 style={heading}>Contato</h4>
          <ul style={{ listStyle: 'none', display: 'flex', flexDirection: 'column', gap: '.75rem' }}>
            <ContactItem icon={<MapPin size={15} />}>
              Av. das Locadoras, 1500 — Centro<br />Belo Horizonte, MG — CEP 30100-000
            </ContactItem>
            <ContactItem icon={<Phone size={15} />}>
              (31) 9 9999-0000
            </ContactItem>
            <ContactItem icon={<Mail size={15} />}>
              contato@rodamundo.com.br
            </ContactItem>
            <ContactItem icon={<Clock size={15} />}>
              Seg–Sex: 08h–18h<br />Sáb: 08h–13h
            </ContactItem>
          </ul>
        </div>

        {/* Horários / info extra */}
        <div>
          <h4 style={heading}>Por que nos escolher?</h4>
          <ul style={{ listStyle: 'none', display: 'flex', flexDirection: 'column', gap: '.6rem' }}>
            {[
              '✅ Frota revisada e higienizada',
              '✅ Preço transparente, sem surpresas',
              '✅ Reserva online em minutos',
              '✅ Seguro incluso em todos os planos',
              '✅ Atendimento humanizado',
            ].map(t => (
              <li key={t} style={{ fontSize: '.835rem', color: '#94a3b8' }}>{t}</li>
            ))}
          </ul>
        </div>

      </div>

      {/* Linha inferior */}
      <div style={{ borderTop: '1px solid rgba(255,255,255,.08)', padding: '1.1rem 1.5rem', display: 'flex', flexWrap: 'wrap', alignItems: 'center', justifyContent: 'space-between', gap: '.5rem', maxWidth: 1280, margin: '0 auto' }}>
        <span style={{ fontSize: '.78rem', color: '#475569' }}>
          © {new Date().getFullYear()} Roda Mundo Aluguel. Todos os direitos reservados.
        </span>
        <span style={{ fontSize: '.78rem', color: '#475569' }}>
          CNPJ: 00.000.000/0001-00 &nbsp;·&nbsp;
          <a href="/estrategia" style={{ color: '#475569', textDecoration: 'underline', textUnderlineOffset: 3 }}
            onMouseEnter={e => e.target.style.color = '#94a3b8'}
            onMouseLeave={e => e.target.style.color = '#475569'}>
            Documentação Técnica & Estratégia
          </a>
        </span>
      </div>
    </footer>
  );
}

function ContactItem({ icon, children }) {
  return (
    <li style={{ display: 'flex', gap: '.6rem', alignItems: 'flex-start', fontSize: '.845rem', color: '#94a3b8', lineHeight: 1.6 }}>
      <span style={{ marginTop: '.15rem', flexShrink: 0, color: '#60a5fa' }}>{icon}</span>
      <span>{children}</span>
    </li>
  );
}

function SocialBtn({ href, label, children }) {
  return (
    <a href={href} aria-label={label} style={{
      width: 36, height: 36, borderRadius: '50%',
      background: 'rgba(255,255,255,.08)', border: '1px solid rgba(255,255,255,.12)',
      display: 'flex', alignItems: 'center', justifyContent: 'center',
      color: '#94a3b8', transition: 'all .15s',
    }}
      onMouseEnter={e => { e.currentTarget.style.background = 'rgba(96,165,250,.2)'; e.currentTarget.style.color = '#60a5fa'; }}
      onMouseLeave={e => { e.currentTarget.style.background = 'rgba(255,255,255,.08)'; e.currentTarget.style.color = '#94a3b8'; }}
    >
      {children}
    </a>
  );
}

const heading = { fontSize: '.78rem', fontWeight: 700, textTransform: 'uppercase', letterSpacing: '.08em', color: '#f1f5f9', marginBottom: '1rem' };
