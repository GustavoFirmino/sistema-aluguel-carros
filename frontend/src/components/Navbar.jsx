import { Link, NavLink, useNavigate } from 'react-router-dom';
import { LogOut, ClipboardList, User, ChevronRight, UserCircle } from 'lucide-react';

const NAV_LINKS = {
  admin: [
    { to: '/dashboard',  label: 'Dashboard'  },
    { to: '/clientes',   label: 'Clientes'   },
    { to: '/automoveis', label: 'Automóveis' },
    { to: '/agentes',    label: 'Agentes'    },
    { to: '/pedidos',    label: 'Pedidos'    },
    { to: '/contratos',  label: 'Contratos'  },
  ],
  agente: [
    { to: '/dashboard',  label: 'Dashboard'  },
    { to: '/clientes',   label: 'Clientes'   },
    { to: '/automoveis', label: 'Automóveis' },
    { to: '/pedidos',    label: 'Pedidos'    },
    { to: '/contratos',  label: 'Contratos'  },
  ],
  banco: [
    { to: '/banco/pedidos', label: 'Análise de Crédito' },
    { to: '/automoveis',    label: 'Frota'               },
  ],
  empresa: [
    { to: '/empresa/pedidos', label: 'Pedidos Aprovados' },
    { to: '/contratos',       label: 'Contratos'         },
    { to: '/automoveis',      label: 'Frota'             },
  ],
  cliente: [
    { to: '/',            label: 'Veículos'     },
    { to: '/meus-pedidos',label: 'Meus Pedidos' },
    { to: '/perfil',      label: 'Perfil'       },
  ],
};

const ROLE_LABELS = { admin: 'Admin', agente: 'Agente', banco: 'Banco', empresa: 'Empresa', cliente: 'Cliente' };
const ROLE_COLORS = {
  admin:   { bg: '#eff6ff', color: '#1d4ed8' },
  agente:  { bg: '#f0fdf4', color: '#15803d' },
  banco:   { bg: '#eff6ff', color: '#2563eb' },
  empresa: { bg: '#faf5ff', color: '#7c3aed' },
  cliente: { bg: '#f0fdf4', color: '#15803d' },
};

export default function Navbar({ usuario, onLogout }) {
  const navigate = useNavigate();
  const role = usuario?.role;
  const links = NAV_LINKS[role] ?? [];
  const roleColor = ROLE_COLORS[role] ?? { bg: '#f3f4f6', color: '#374151' };

  const handleLogout = () => { onLogout(); navigate('/'); };

  return (
    <nav style={{
      background: 'rgba(255,255,255,.97)',
      backdropFilter: 'blur(14px)',
      WebkitBackdropFilter: 'blur(14px)',
      borderBottom: '1px solid rgba(229,231,235,.7)',
      position: 'sticky', top: 0, zIndex: 100,
      boxShadow: '0 1px 12px rgba(0,0,0,.07)',
    }}>
      <div style={{
        maxWidth: 1280, margin: '0 auto',
        padding: '0 1.5rem',
        height: 68,
        display: 'flex', alignItems: 'center', gap: '1.5rem',
      }}>

        {/* Logo */}
        <Link to={role === 'admin' || role === 'agente' ? '/dashboard' : role === 'banco' ? '/banco/pedidos' : role === 'empresa' ? '/empresa/pedidos' : '/'}>
          <img
            src="/images/logo.jpeg"
            alt="Roda Mundo Aluguel"
            style={{ height: 52, width: 'auto', objectFit: 'contain', borderRadius: 6, flexShrink: 0 }}
          />
        </Link>

        {/* Nav links */}
        <div style={{ display: 'flex', gap: '.15rem', flex: 1, flexWrap: 'wrap' }}>
          {links.map(({ to, label }) => (
            <NavLink
              key={to}
              to={to}
              end={to === '/'}
              style={({ isActive }) => ({
                padding: '.38rem .8rem',
                borderRadius: 7,
                fontSize: '.84rem',
                fontWeight: isActive ? 600 : 500,
                color: isActive ? '#1d4ed8' : '#4b5563',
                background: isActive ? '#eff6ff' : 'transparent',
                transition: 'all .15s',
                whiteSpace: 'nowrap',
              })}
              onMouseEnter={e => { if (!e.currentTarget.classList.contains('active')) { e.currentTarget.style.background = '#f9fafb'; e.currentTarget.style.color = '#111827'; }}}
              onMouseLeave={e => { if (!e.currentTarget.classList.contains('active')) { e.currentTarget.style.background = 'transparent'; e.currentTarget.style.color = '#4b5563'; }}}
            >
              {label}
            </NavLink>
          ))}
        </div>

        {/* Auth */}
        <div style={{ display: 'flex', alignItems: 'center', gap: '.6rem', flexShrink: 0 }}>
          {usuario ? (
            <>
              {/* Avatar + nome + badge */}
              <div style={{ display: 'flex', alignItems: 'center', gap: '.5rem', padding: '.3rem .75rem', borderRadius: 999, background: '#f1f5f9' }}>
                <div style={{ width: 28, height: 28, borderRadius: '50%', background: 'linear-gradient(135deg, #1d4ed8, #7c3aed)', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
                  <span style={{ fontSize: '.7rem', fontWeight: 700, color: '#fff' }}>
                    {usuario.nome?.charAt(0).toUpperCase()}
                  </span>
                </div>
                <span style={{ fontSize: '.82rem', fontWeight: 600, color: '#374151' }}>
                  {usuario.nome?.split(' ')[0]}
                </span>
                {role && (
                  <span style={{ fontSize: '.68rem', fontWeight: 700, padding: '.15rem .45rem', borderRadius: 999, background: roleColor.bg, color: roleColor.color, textTransform: 'uppercase', letterSpacing: '.04em' }}>
                    {ROLE_LABELS[role]}
                  </span>
                )}
              </div>

              <button onClick={handleLogout}
                style={{ ...btnOutlineStyle, color: '#dc2626', borderColor: '#fecaca' }}
                onMouseEnter={e => e.currentTarget.style.background = '#fef2f2'}
                onMouseLeave={e => e.currentTarget.style.background = '#fff'}
                title="Sair">
                <LogOut size={14} /> Sair
              </button>
            </>
          ) : (
            <>
              <Link to="/login" style={{ textDecoration: 'none' }}>
                <button style={btnOutlineStyle} onMouseEnter={hoverIn} onMouseLeave={hoverOut}>
                  <User size={14} /> Entrar
                </button>
              </Link>
              <Link to="/cadastro" style={{ textDecoration: 'none' }}>
                <button style={btnPrimaryStyle}
                  onMouseEnter={e => { e.currentTarget.style.background = '#1d4ed8'; e.currentTarget.style.boxShadow = '0 4px 14px rgba(29,78,216,.5)'; }}
                  onMouseLeave={e => { e.currentTarget.style.background = '#2563eb'; e.currentTarget.style.boxShadow = '0 2px 8px rgba(37,99,235,.35)'; }}>
                  Criar conta <ChevronRight size={13} />
                </button>
              </Link>
            </>
          )}
        </div>
      </div>
    </nav>
  );
}

const btnOutlineStyle = {
  display: 'inline-flex', alignItems: 'center', gap: '.35rem',
  padding: '.38rem .85rem', borderRadius: 7,
  border: '1.5px solid #e5e7eb', background: '#fff',
  fontSize: '.82rem', fontWeight: 500, color: '#374151',
  cursor: 'pointer', fontFamily: 'inherit', transition: 'all .15s',
};

const btnPrimaryStyle = {
  display: 'inline-flex', alignItems: 'center', gap: '.3rem',
  padding: '.42rem 1rem', borderRadius: 7,
  border: 'none', background: '#2563eb',
  fontSize: '.82rem', fontWeight: 600, color: '#fff',
  cursor: 'pointer', fontFamily: 'inherit',
  boxShadow: '0 2px 8px rgba(37,99,235,.35)', transition: 'all .15s',
};

const hoverIn  = e => { e.currentTarget.style.background = '#f9fafb'; e.currentTarget.style.borderColor = '#d1d5db'; };
const hoverOut = e => { e.currentTarget.style.background = '#fff';    e.currentTarget.style.borderColor = '#e5e7eb'; };
