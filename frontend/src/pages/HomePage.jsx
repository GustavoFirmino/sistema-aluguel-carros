import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Car, Search, ChevronRight, SlidersHorizontal, Star, Shield, Clock, ChevronLeft } from 'lucide-react';
import toast from 'react-hot-toast';
import { automovelService } from '../services/api';
import NovoPedidoModal from '../components/pedido/NovoPedidoModal';
import Carousel from '../components/Carousel';

const API = 'http://localhost:8080';
const fmt = (v) => new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(v);

const MARCAS = ['Todas', 'Chevrolet', 'Fiat', 'Ford', 'Honda', 'Hyundai', 'Jeep', 'Nissan', 'Renault', 'Toyota', 'Volkswagen'];
const CORES  = ['Todas', 'Azul', 'Branco', 'Cinza', 'Laranja', 'Preto', 'Prata', 'Roxo', 'Vermelho', 'Verde'];
const ANOS   = ['Todos', '2024', '2023', '2022', '2021', '2020', '2019'];
const POR_PAGINA = 8;

// Fallback por marca (Unsplash) quando não há foto no backend
const FOTOS_FALLBACK = {
  Toyota: 'https://images.unsplash.com/photo-1621007947382-bb3c3994e3fb?w=600&h=340&fit=crop&auto=format',
  Honda:  'https://images.unsplash.com/photo-1606664515524-ed2f786a0bd6?w=600&h=340&fit=crop&auto=format',
  Volkswagen:'https://images.unsplash.com/photo-1612825173281-9a193378527e?w=600&h=340&fit=crop&auto=format',
  Ford:   'https://images.unsplash.com/photo-1551830820-959a573b2b3c?w=600&h=340&fit=crop&auto=format',
  Chevrolet:'https://images.unsplash.com/photo-1552519507-da3b142c6e3d?w=600&h=340&fit=crop&auto=format',
  Hyundai:'https://images.unsplash.com/photo-1629897048514-3dd7414fe72a?w=600&h=340&fit=crop&auto=format',
  Jeep:   'https://images.unsplash.com/photo-1533473359331-0135ef1b58bf?w=600&h=340&fit=crop&auto=format',
  Fiat:   'https://images.unsplash.com/photo-1580274455191-1c62282b63f5?w=600&h=340&fit=crop&auto=format',
  Renault:'https://images.unsplash.com/photo-1494905998402-395d579af36f?w=600&h=340&fit=crop&auto=format',
  Nissan: 'https://images.unsplash.com/photo-1600706432502-77a0e2c79ae3?w=600&h=340&fit=crop&auto=format',
  default:'https://images.unsplash.com/photo-1568605117036-5fe5e7bab0b7?w=600&h=340&fit=crop&auto=format',
};

function CarCard({ carro, onAlugar, escasso }) {
  const disponivel = carro.disponivel;
  // Tenta a foto do backend; se não existir, cai no fallback por marca
  const [src, setSrc] = useState(`${API}${carro.fotoUrl}`);

  const fallback = () => setSrc(FOTOS_FALLBACK[carro.marca] || FOTOS_FALLBACK.default);

  return (
    <div style={{
      background: '#fff', borderRadius: 14, overflow: 'hidden',
      boxShadow: '0 2px 12px rgba(0,0,0,.07)', border: '1px solid #f1f5f9',
      display: 'flex', flexDirection: 'column',
      transition: 'transform .2s, box-shadow .2s',
    }}
      onMouseEnter={e => { e.currentTarget.style.transform = 'translateY(-5px)'; e.currentTarget.style.boxShadow = '0 18px 40px rgba(0,0,0,.13)'; }}
      onMouseLeave={e => { e.currentTarget.style.transform = 'translateY(0)';    e.currentTarget.style.boxShadow = '0 2px 12px rgba(0,0,0,.07)'; }}
    >
      {/* Foto */}
      <div style={{ position: 'relative', height: 180, overflow: 'hidden', background: '#f8fafc' }}>
        <img src={src} alt={`${carro.marca} ${carro.modelo}`} onError={fallback}
          style={{ width: '100%', height: '100%', objectFit: 'cover', display: 'block' }} />
        <div style={{ position: 'absolute', bottom: 0, left: 0, right: 0, height: 70, background: 'linear-gradient(transparent, rgba(0,0,0,.55))' }} />
        <span style={{
          position: 'absolute', top: 10, right: 10,
          padding: '.2rem .7rem', borderRadius: 999,
          fontSize: '.65rem', fontWeight: 700, textTransform: 'uppercase',
          background: disponivel ? 'rgba(22,163,74,.92)' : 'rgba(220,38,38,.88)',
          color: '#fff', backdropFilter: 'blur(4px)',
        }}>
          {disponivel ? '● Disponível' : '● Indisponível'}
        </span>
        {disponivel && escasso && (
          <span style={{
            position: 'absolute', top: 10, left: 10,
            padding: '.2rem .6rem', borderRadius: 999,
            fontSize: '.62rem', fontWeight: 700,
            background: 'rgba(234,88,12,.92)', color: '#fff', backdropFilter: 'blur(4px)',
          }}>
            🔥 Última unidade
          </span>
        )}
        <div style={{ position: 'absolute', bottom: 8, left: 12 }}>
          <span style={{ fontSize: '1.2rem', fontWeight: 800, color: '#fff', textShadow: '0 1px 4px rgba(0,0,0,.5)' }}>
            {fmt(carro.valorDiaria ?? 0)}
          </span>
          <span style={{ fontSize: '.72rem', color: 'rgba(255,255,255,.8)', marginLeft: '.25rem' }}>/dia</span>
        </div>
      </div>

      {/* Info */}
      <div style={{ padding: '1rem 1.1rem 1.1rem', flex: 1, display: 'flex', flexDirection: 'column', gap: '.5rem' }}>
        <div>
          <div style={{ fontWeight: 700, fontSize: '1rem', color: '#0f172a', lineHeight: 1.2 }}>
            {carro.marca} {carro.modelo}
          </div>
          {/* Especificações */}
          <div style={{ display: 'flex', flexWrap: 'wrap', gap: '.4rem', marginTop: '.45rem' }}>
            <Spec label={carro.ano} />
            <Spec label={carro.placa} mono />
            {carro.cor && <Spec label={carro.cor} color />}
          </div>
        </div>

        <button onClick={() => onAlugar(carro)} disabled={!disponivel}
          style={{
            marginTop: 'auto', width: '100%', padding: '.6rem',
            borderRadius: 9, border: 'none',
            background: disponivel ? 'linear-gradient(135deg, #f97316, #ea6c00)' : '#f1f5f9',
            color: disponivel ? '#fff' : '#94a3b8',
            fontWeight: 700, fontSize: '.875rem',
            cursor: disponivel ? 'pointer' : 'not-allowed',
            display: 'flex', alignItems: 'center', justifyContent: 'center', gap: '.4rem',
            fontFamily: 'inherit',
            boxShadow: disponivel ? '0 2px 8px rgba(249,115,22,.4)' : 'none',
          }}
          onMouseEnter={e => { if (disponivel) e.currentTarget.style.opacity = '.88'; }}
          onMouseLeave={e => { if (disponivel) e.currentTarget.style.opacity = '1'; }}
        >
          {disponivel ? <><ChevronRight size={15} /> Reservar agora</> : 'Indisponível'}
        </button>
      </div>
    </div>
  );
}

function Spec({ label, mono, color }) {
  return (
    <span style={{
      fontSize: '.73rem', fontWeight: 500,
      background: color ? '#f0fdf4' : '#f1f5f9',
      color: color ? '#16a34a' : '#64748b',
      padding: '.1rem .45rem', borderRadius: 5,
      fontFamily: mono ? 'monospace' : 'inherit',
      border: color ? '1px solid #bbf7d0' : 'none',
    }}>
      {label}
    </span>
  );
}

export default function HomePage({ usuario }) {
  const navigate = useNavigate();
  const [carros, setCarros]           = useState([]);
  const [loading, setLoading]         = useState(true);
  const [search, setSearch]           = useState('');
  const [marcaFiltro, setMarcaFiltro] = useState('Todas');
  const [corFiltro,  setCorFiltro]    = useState('Todas');
  const [anoFiltro,  setAnoFiltro]    = useState('Todos');
  const [soDisp, setSoDisp]           = useState(false);
  const [pagina, setPagina]           = useState(1);
  const [carroParaAlugar, setCarroParaAlugar] = useState(null);

  useEffect(() => {
    automovelService.listar()
      .then(r => setCarros(Array.isArray(r.data) ? r.data : (r.data.items ?? [])))
      .catch(() => toast.error('Erro ao carregar veículos.'))
      .finally(() => setLoading(false));
  }, []);

  // Reseta página ao mudar filtros
  useEffect(() => { setPagina(1); }, [search, marcaFiltro, corFiltro, anoFiltro, soDisp]);

  const filtered = carros.filter(c => {
    const texto = `${c.marca} ${c.modelo} ${c.placa} ${c.cor ?? ''}`.toLowerCase();
    return texto.includes(search.toLowerCase())
      && (marcaFiltro === 'Todas' || c.marca === marcaFiltro)
      && (corFiltro === 'Todas'   || c.cor === corFiltro)
      && (anoFiltro === 'Todos'   || String(c.ano) === anoFiltro)
      && (!soDisp || c.disponivel);
  });

  const totalPaginas = Math.max(1, Math.ceil(filtered.length / POR_PAGINA));
  const pagina_ok    = Math.min(pagina, totalPaginas);
  const slice        = filtered.slice((pagina_ok - 1) * POR_PAGINA, pagina_ok * POR_PAGINA);

  // Conta quantos carros disponíveis existem por modelo — 1 = última unidade
  const disponivelPorModelo = carros.reduce((acc, c) => {
    if (c.disponivel) {
      const chave = `${c.marca}|${c.modelo}`;
      acc[chave] = (acc[chave] || 0) + 1;
    }
    return acc;
  }, {});

  const handleAlugar = (carro) => {
    if (!usuario) { toast('Faça login para reservar.', { icon: '🔐' }); navigate('/login'); return; }
    setCarroParaAlugar(carro);
  };

  const selectStyle = {
    padding: '.42rem .7rem', border: '1.5px solid #e2e8f0', borderRadius: 8,
    fontSize: '.84rem', color: '#374151', background: '#f8fafc',
    outline: 'none', fontFamily: 'inherit', flex: '0 1 140px',
  };

  return (
    <div style={{ background: '#f8fafc' }}>

      {/* Prova Social */}
      <div style={{ background: 'linear-gradient(90deg, #1e3a8a 0%, #2563eb 100%)', color: '#fff' }}>
        <div style={{ maxWidth: 1280, margin: '0 auto', padding: '.9rem 1.5rem', display: 'flex', flexWrap: 'wrap', gap: '1.5rem', alignItems: 'center', justifyContent: 'center' }}>
          {[
            { stat: '★ 4.8/5', label: '2.500+ avaliações' },
            { stat: '98%',     label: 'de clientes satisfeitos' },
            { stat: '+12.000', label: 'aluguéis realizados' },
            { stat: '< 5 min', label: 'para reservar' },
          ].map(({ stat, label }) => (
            <div key={stat} style={{ display: 'flex', alignItems: 'center', gap: '.5rem', fontSize: '.85rem' }}>
              <span style={{ fontWeight: 800, fontSize: '1.05rem' }}>{stat}</span>
              <span style={{ opacity: .75 }}>{label}</span>
            </div>
          ))}
        </div>
      </div>

      {/* Carrossel */}
      <Carousel />

      {/* Quem Somos */}
      <div style={{ background: '#fff', borderBottom: '1px solid #f1f5f9' }}>
        <div style={{ maxWidth: 900, margin: '0 auto', padding: 'clamp(2rem, 5vw, 3.5rem) 1.5rem', textAlign: 'center' }}>
          <div style={{ display: 'inline-flex', alignItems: 'center', gap: '.45rem', background: '#eff6ff', border: '1px solid #bfdbfe', borderRadius: 999, padding: '.3rem .9rem', marginBottom: '1.25rem' }}>
            <span style={{ fontSize: '.72rem', fontWeight: 700, textTransform: 'uppercase', letterSpacing: '.07em', color: '#1d4ed8' }}>Quem somos</span>
          </div>
          <h2 style={{ fontSize: 'clamp(1.4rem, 3.5vw, 2.1rem)', fontWeight: 900, color: '#0f172a', letterSpacing: '-.03em', lineHeight: 1.2, marginBottom: '1.1rem' }}>
            Mobilidade com confiança,<br />desde o primeiro clique
          </h2>
          <p style={{ fontSize: 'clamp(.88rem, 1.5vw, 1rem)', color: '#4b5563', lineHeight: 1.75, maxWidth: 680, margin: '0 auto 1.75rem' }}>
            A <strong style={{ color: '#0f172a' }}>Roda Mundo</strong> nasceu da crença de que alugar um carro deveria ser tão simples quanto pedir um táxi.
            Percebemos que o processo tradicional — burocracia, filas e surpresas no valor final — frustrava quem só queria chegar ao destino.
            Por isso criamos uma plataforma onde o cliente escolhe, reserva e dirige em minutos, com <strong style={{ color: '#0f172a' }}>preço transparente, seguro incluso e atendimento humano</strong>.
          </p>
          <div style={{ display: 'flex', flexWrap: 'wrap', gap: '1rem', justifyContent: 'center' }}>
            {[
              { num: '2018',   label: 'Fundada em Belo Horizonte' },
              { num: '12 mil', label: 'Aluguéis realizados' },
              { num: '98%',    label: 'Clientes satisfeitos' },
              { num: '24/7',   label: 'Suporte disponível' },
            ].map(({ num, label }) => (
              <div key={label} style={{ flex: '1 1 120px', maxWidth: 160, background: '#f8fafc', border: '1px solid #e2e8f0', borderRadius: 12, padding: '1rem .75rem' }}>
                <div style={{ fontSize: 'clamp(1.2rem, 2.5vw, 1.6rem)', fontWeight: 900, color: '#2563eb', lineHeight: 1 }}>{num}</div>
                <div style={{ fontSize: '.75rem', color: '#64748b', marginTop: '.35rem', lineHeight: 1.4 }}>{label}</div>
              </div>
            ))}
          </div>
        </div>
      </div>

      {/* Diferenciais */}
      <div style={{ background: '#fff', borderBottom: '1px solid #f1f5f9' }}>
        <div style={{ maxWidth: 1280, margin: '0 auto', padding: '1.25rem 1.5rem', display: 'flex', flexWrap: 'wrap', gap: '1rem', justifyContent: 'center' }}>
          {[
            { icon: Star,   color: '#f59e0b', bg: '#fffbeb', label: 'Frota premium',  desc: 'Veículos revisados e higienizados' },
            { icon: Shield, color: '#10b981', bg: '#ecfdf5', label: 'Seguro incluso', desc: 'Proteção total durante o período' },
            { icon: Clock,  color: '#6366f1', bg: '#eef2ff', label: 'Processo ágil',  desc: 'Reserve em menos de 5 minutos' },
          ].map(({ icon: Icon, color, bg, label, desc }) => (
            <div key={label} style={{ display: 'flex', alignItems: 'center', gap: '.85rem', border: '1px solid #f1f5f9', borderRadius: 12, padding: '.85rem 1.25rem', flex: '1 1 200px', maxWidth: 320 }}>
              <div style={{ width: 40, height: 40, borderRadius: 10, background: bg, display: 'flex', alignItems: 'center', justifyContent: 'center', flexShrink: 0 }}>
                <Icon size={19} color={color} />
              </div>
              <div>
                <div style={{ fontWeight: 700, fontSize: '.88rem', color: '#0f172a' }}>{label}</div>
                <div style={{ fontSize: '.76rem', color: '#64748b', marginTop: '.1rem' }}>{desc}</div>
              </div>
            </div>
          ))}
        </div>
      </div>

      {/* Como funciona */}
      <div style={{ background: '#f8fafc', borderBottom: '1px solid #f1f5f9' }}>
        <div style={{ maxWidth: 1280, margin: '0 auto', padding: '2.5rem 1.5rem', textAlign: 'center' }}>
          <h2 style={{ fontSize: 'clamp(1.1rem, 2vw, 1.4rem)', fontWeight: 800, color: '#0f172a', marginBottom: '.4rem' }}>Como funciona?</h2>
          <p style={{ color: '#64748b', fontSize: '.87rem', marginBottom: '2rem' }}>Reserve o seu veículo em 3 passos simples</p>
          <div style={{ display: 'flex', flexWrap: 'wrap', gap: '2rem', justifyContent: 'center', position: 'relative' }}>
            {[
              { step: '1', title: 'Crie sua conta',       desc: 'Cadastre-se em segundos com nome de usuário e senha.', highlight: false },
              { step: '2', title: 'Complete seu perfil',  desc: 'Informe seu CPF, renda mensal e selecione seu banco parceiro. Você será redirecionado automaticamente após o cadastro.', highlight: true },
              { step: '3', title: 'Escolha o veículo',    desc: 'Filtre por marca, cor ou ano e encontre o carro ideal para a sua viagem.', highlight: false },
              { step: '4', title: 'Aguarde a aprovação',  desc: 'O banco analisa seu pedido. Após aprovação, o contrato é gerado e você já pode retirar o veículo.', highlight: false },
            ].map(({ step, title, desc, highlight }) => (
              <div key={step} style={{ flex: '1 1 180px', maxWidth: 230, display: 'flex', flexDirection: 'column', alignItems: 'center', gap: '.75rem' }}>
                <div style={{
                  width: 48, height: 48, borderRadius: '50%',
                  background: highlight ? 'linear-gradient(135deg, #f97316, #ea6c00)' : 'linear-gradient(135deg, #2563eb, #4f46e5)',
                  color: '#fff', fontWeight: 800, fontSize: '1.1rem',
                  display: 'flex', alignItems: 'center', justifyContent: 'center',
                  boxShadow: highlight ? '0 4px 12px rgba(249,115,22,.4)' : '0 4px 12px rgba(37,99,235,.35)',
                }}>
                  {step}
                </div>
                <div style={{ fontWeight: 700, fontSize: '.95rem', color: '#0f172a' }}>
                  {title}
                  {highlight && <span style={{ display: 'inline-block', marginLeft: '.4rem', fontSize: '.65rem', fontWeight: 700, background: '#fff7ed', color: '#f97316', border: '1px solid #fed7aa', borderRadius: 999, padding: '.1rem .45rem', verticalAlign: 'middle' }}>Obrigatório</span>}
                </div>
                <div style={{ fontSize: '.82rem', color: '#64748b', lineHeight: 1.6 }}>{desc}</div>
              </div>
            ))}
          </div>
        </div>
      </div>

      {/* Frota */}
      <div style={{ maxWidth: 1280, margin: '0 auto', padding: '2.5rem 1.5rem' }}>

        {/* Título */}
        <div style={{ marginBottom: '1.75rem', display: 'flex', alignItems: 'center', gap: '.75rem' }}>
          <div style={{ width: 4, height: 28, background: 'linear-gradient(180deg, #2563eb, #4f46e5)', borderRadius: 2 }} />
          <div>
            <h2 style={{ fontSize: 'clamp(1.3rem, 2.5vw, 1.8rem)', fontWeight: 900, color: '#0f172a', letterSpacing: '-.03em' }}>
              Conheça nossa frota
            </h2>
            <p style={{ color: '#64748b', fontSize: '.88rem', marginTop: '.15rem' }}>
              Escolha o veículo ideal para a sua viagem
            </p>
          </div>
        </div>

        {/* Filtros */}
        <div style={{ background: '#fff', borderRadius: 12, border: '1px solid #e2e8f0', padding: '1rem 1.25rem', marginBottom: '1.75rem', display: 'flex', flexWrap: 'wrap', gap: '.6rem', alignItems: 'center', boxShadow: '0 1px 4px rgba(0,0,0,.04)' }}>
          <SlidersHorizontal size={15} color="#64748b" />

          {/* Busca */}
          <div style={{ position: 'relative', flex: '1 1 200px' }}>
            <Search size={14} style={{ position: 'absolute', left: '.65rem', top: '50%', transform: 'translateY(-50%)', color: '#9ca3af' }} />
            <input style={{ width: '100%', padding: '.42rem .75rem .42rem 2rem', border: '1.5px solid #e2e8f0', borderRadius: 8, fontSize: '.84rem', color: '#0f172a', outline: 'none', fontFamily: 'inherit', background: '#f8fafc' }}
              placeholder="Marca, modelo, placa, cor..."
              value={search} onChange={e => setSearch(e.target.value)}
              onFocus={e => e.target.style.borderColor = '#2563eb'}
              onBlur={e => e.target.style.borderColor = '#e2e8f0'}
            />
          </div>

          <select style={selectStyle} value={marcaFiltro} onChange={e => setMarcaFiltro(e.target.value)}>
            {MARCAS.map(m => <option key={m} value={m}>{m === 'Todas' ? 'Todas as marcas' : m}</option>)}
          </select>

          <select style={selectStyle} value={corFiltro} onChange={e => setCorFiltro(e.target.value)}>
            {CORES.map(c => <option key={c} value={c}>{c === 'Todas' ? 'Todas as cores' : c}</option>)}
          </select>

          <select style={{ ...selectStyle, flex: '0 1 110px' }} value={anoFiltro} onChange={e => setAnoFiltro(e.target.value)}>
            {ANOS.map(a => <option key={a} value={a}>{a === 'Todos' ? 'Todos os anos' : a}</option>)}
          </select>

          <label style={{ display: 'flex', alignItems: 'center', gap: '.4rem', fontSize: '.84rem', fontWeight: 500, color: '#374151', cursor: 'pointer', whiteSpace: 'nowrap' }}>
            <input type="checkbox" checked={soDisp} onChange={e => setSoDisp(e.target.checked)} />
            Só disponíveis
          </label>

          <span style={{ fontSize: '.8rem', color: '#94a3b8', marginLeft: 'auto', whiteSpace: 'nowrap' }}>
            {filtered.length} veículo{filtered.length !== 1 ? 's' : ''}
          </span>
        </div>

        {/* Grid */}
        {loading ? (
          <div style={{ display: 'flex', justifyContent: 'center', padding: '4rem 0' }}>
            <div className="spinner" />
          </div>
        ) : filtered.length === 0 ? (
          <div className="empty-state" style={{ padding: '3rem 0' }}>
            <Car size={44} />
            <p>Nenhum veículo encontrado com esses filtros.</p>
          </div>
        ) : (
          <>
            <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(260px, 1fr))', gap: '1.25rem', marginBottom: '2rem' }}>
              {slice.map(c => (
                <CarCard
                  key={c.id}
                  carro={c}
                  onAlugar={handleAlugar}
                  escasso={disponivelPorModelo[`${c.marca}|${c.modelo}`] === 1}
                />
              ))}
            </div>

            {/* Paginação */}
            {totalPaginas > 1 && (
              <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', gap: '.5rem' }}>
                <PagBtn onClick={() => setPagina(p => Math.max(1, p - 1))} disabled={pagina_ok === 1}>
                  <ChevronLeft size={15} />
                </PagBtn>

                {Array.from({ length: totalPaginas }, (_, i) => i + 1).map(n => (
                  <PagBtn key={n} onClick={() => setPagina(n)} active={n === pagina_ok}>
                    {n}
                  </PagBtn>
                ))}

                <PagBtn onClick={() => setPagina(p => Math.min(totalPaginas, p + 1))} disabled={pagina_ok === totalPaginas}>
                  <ChevronRight size={15} />
                </PagBtn>
              </div>
            )}
          </>
        )}

        {/* CTA para visitantes */}
        {!usuario && !loading && carros.length > 0 && (
          <div style={{ marginTop: '3rem', padding: '2rem', borderRadius: 16, textAlign: 'center', background: 'linear-gradient(135deg, #1e3a8a, #2563eb)', boxShadow: '0 8px 32px rgba(37,99,235,.3)' }}>
            <h3 style={{ fontWeight: 800, color: '#fff', fontSize: '1.2rem', marginBottom: '.5rem' }}>Pronto para reservar?</h3>
            <p style={{ color: 'rgba(255,255,255,.75)', fontSize: '.9rem', marginBottom: '1.25rem' }}>Crie sua conta gratuitamente e alugue em minutos.</p>
            <div style={{ display: 'flex', gap: '.75rem', justifyContent: 'center', flexWrap: 'wrap' }}>
              <a href="/cadastro" style={{ padding: '.6rem 1.4rem', borderRadius: 8, background: '#f97316', color: '#fff', fontWeight: 700, fontSize: '.875rem', textDecoration: 'none', boxShadow: '0 2px 8px rgba(249,115,22,.45)' }}>Criar conta grátis</a>
              <a href="/login" style={{ padding: '.6rem 1.4rem', borderRadius: 8, border: '1.5px solid rgba(255,255,255,.4)', color: '#fff', fontWeight: 600, fontSize: '.875rem', textDecoration: 'none' }}>Já tenho conta</a>
            </div>
          </div>
        )}
      </div>

      {carroParaAlugar && usuario && (
        <NovoPedidoModal
          automovelPreSelecionado={carroParaAlugar}
          onClose={() => setCarroParaAlugar(null)}
          onSave={() => { setCarroParaAlugar(null); toast.success('Pedido criado!'); }}
        />
      )}
    </div>
  );
}

function PagBtn({ children, onClick, disabled, active }) {
  return (
    <button onClick={onClick} disabled={disabled}
      style={{
        minWidth: 36, height: 36, padding: '0 .5rem',
        borderRadius: 8, border: active ? 'none' : '1.5px solid #e2e8f0',
        background: active ? '#2563eb' : '#fff',
        color: active ? '#fff' : disabled ? '#cbd5e1' : '#374151',
        fontWeight: active ? 700 : 500, fontSize: '.85rem',
        cursor: disabled ? 'not-allowed' : 'pointer',
        display: 'flex', alignItems: 'center', justifyContent: 'center',
        fontFamily: 'inherit', transition: 'all .15s',
        boxShadow: active ? '0 2px 8px rgba(37,99,235,.35)' : 'none',
      }}>
      {children}
    </button>
  );
}
