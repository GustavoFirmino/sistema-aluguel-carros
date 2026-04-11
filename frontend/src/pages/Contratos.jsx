import { useState, useEffect } from 'react';
import { contratoService, agenteService } from '../services/api';
import toast from 'react-hot-toast';
import { ScrollText, CreditCard, ChevronDown, ChevronUp } from 'lucide-react';

export default function Contratos() {
  const [contratos, setContratos] = useState([]);
  const [bancos, setBancos] = useState([]);
  const [loading, setLoading] = useState(true);
  const [expanded, setExpanded] = useState(null);
  const [creditoModal, setCreditoModal] = useState(null);
  const [bancoId, setBancoId] = useState('');
  const [saving, setSaving] = useState(false);

  const carregar = async () => {
    setLoading(true);
    try {
      const [{ data: ctrs }, { data: bks }] = await Promise.all([
        contratoService.listar(),
        agenteService.listar('BANCO'),
      ]);
      setContratos(ctrs);
      setBancos(bks);
    } catch {
      toast.error('Erro ao carregar contratos.');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { carregar(); }, []);

  const toggleExpand = (id) => setExpanded(prev => prev === id ? null : id);

  const abrirCredito = (contrato) => {
    setCreditoModal(contrato);
    setBancoId('');
  };

  const associarCredito = async (e) => {
    e.preventDefault();
    setSaving(true);
    try {
      await contratoService.associarCredito(creditoModal.id, { bancoAgenteId: Number(bancoId) });
      toast.success('Contrato de crédito associado!');
      setCreditoModal(null);
      carregar();
    } catch (err) {
      toast.error(err.response?.data?.message || 'Erro ao associar crédito.');
    } finally {
      setSaving(false);
    }
  };

  const fmt = (val) => val != null
    ? new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(val)
    : '—';

  const fmtDate = (str) => str
    ? new Date(str + 'T00:00:00').toLocaleDateString('pt-BR')
    : '—';

  return (
    <div className="page">
      <div className="page-header">
        <div>
          <h2 className="page-title">Contratos</h2>
          <p className="page-subtitle">Contratos de aluguel gerados a partir de pedidos aprovados</p>
        </div>
      </div>

      {loading ? (
        <p className="empty-state">Carregando...</p>
      ) : contratos.length === 0 ? (
        <div className="empty-state">
          <ScrollText size={40} style={{ opacity: 0.3, marginBottom: 8 }} />
          <p>Nenhum contrato gerado ainda.</p>
          <p style={{ fontSize: 13, opacity: 0.6 }}>Aprove um pedido e gere o contrato na tela de Pedidos.</p>
        </div>
      ) : (
        <div className="card-list">
          {contratos.map(c => (
            <div key={c.id} className="card contract-card">
              <div className="contract-header" onClick={() => toggleExpand(c.id)}>
                <div className="contract-main">
                  <span className="contract-number">
                    <ScrollText size={15} style={{ marginRight: 6 }} />
                    {c.numeroContrato}
                  </span>
                  <span className="contract-client">{c.clienteNome}</span>
                  <span className="contract-car">{c.automovelDescricao}</span>
                </div>
                <div className="contract-meta">
                  <span className="contract-total">{fmt(c.valorTotal)}</span>
                  <span className={`badge ${c.possuiContratoCredito ? 'badge-success' : 'badge-neutral'}`}>
                    {c.possuiContratoCredito ? 'Com crédito' : 'Sem crédito'}
                  </span>
                  {expanded === c.id ? <ChevronUp size={16} /> : <ChevronDown size={16} />}
                </div>
              </div>

              {expanded === c.id && (
                <div className="contract-details">
                  <div className="detail-grid">
                    <div><span>CPF do cliente</span><strong>{c.clienteCpf}</strong></div>
                    <div><span>Agente responsável</span><strong>{c.agenteNome || '—'}</strong></div>
                    <div><span>Período</span><strong>{c.diasAluguel} dia(s)</strong></div>
                    <div><span>Valor da diária</span><strong>{fmt(c.valorDiaria)}</strong></div>
                    <div><span>Valor total</span><strong>{fmt(c.valorTotal)}</strong></div>
                    <div><span>Data do contrato</span><strong>{fmtDate(c.dataContrato)}</strong></div>
                    {c.possuiContratoCredito && (
                      <div><span>Banco do crédito</span><strong>{c.bancoAgenteNome}</strong></div>
                    )}
                  </div>

                  {!c.possuiContratoCredito && (
                    <div style={{ marginTop: 12 }}>
                      <button className="btn btn-secondary btn-sm" onClick={() => abrirCredito(c)}>
                        <CreditCard size={14} /> Associar contrato de crédito
                      </button>
                    </div>
                  )}
                </div>
              )}
            </div>
          ))}
        </div>
      )}

      {/* Modal Crédito */}
      {creditoModal && (
        <div className="modal-overlay" onClick={() => setCreditoModal(null)}>
          <div className="modal" onClick={e => e.stopPropagation()}>
            <div className="modal-header">
              <h3>Associar Contrato de Crédito</h3>
              <button className="modal-close" onClick={() => setCreditoModal(null)}>×</button>
            </div>
            <form onSubmit={associarCredito} className="modal-body">
              <p style={{ marginBottom: 12, color: '#64748b' }}>
                Contrato <strong>{creditoModal.numeroContrato}</strong> — {creditoModal.clienteNome}
              </p>
              <div className="form-group">
                <label>Banco agente *</label>
                {bancos.length === 0 ? (
                  <p style={{ color: '#ef4444', fontSize: 13 }}>Nenhum banco cadastrado. Cadastre um agente do tipo Banco primeiro.</p>
                ) : (
                  <select className="input" required value={bancoId} onChange={e => setBancoId(e.target.value)}>
                    <option value="">Selecione o banco...</option>
                    {bancos.map(b => (
                      <option key={b.id} value={b.id}>{b.nome} — {b.cnpj}</option>
                    ))}
                  </select>
                )}
              </div>
              <div className="modal-footer">
                <button type="button" className="btn btn-ghost" onClick={() => setCreditoModal(null)}>Cancelar</button>
                <button type="submit" className="btn btn-primary" disabled={saving || bancos.length === 0}>
                  {saving ? 'Salvando...' : 'Associar'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}
