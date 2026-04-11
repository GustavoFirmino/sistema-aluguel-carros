import { useState, useEffect } from 'react';
import { agenteService } from '../services/api';
import toast from 'react-hot-toast';
import { Plus, Pencil, Trash2, Building2, Landmark } from 'lucide-react';
import ConfirmModal from '../components/ConfirmModal';

const TIPO_LABELS = { EMPRESA: 'Empresa', BANCO: 'Banco' };

const emptyForm = { nome: '', cnpj: '', tipo: 'EMPRESA', email: '', telefone: '' };

export default function Agentes() {
  const [agentes, setAgentes] = useState([]);
  const [filtroTipo, setFiltroTipo] = useState('');
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [editando, setEditando] = useState(null);
  const [form, setForm] = useState(emptyForm);
  const [confirmDelete, setConfirmDelete] = useState(null);
  const [saving, setSaving] = useState(false);

  const carregar = async () => {
    setLoading(true);
    try {
      const { data } = await agenteService.listar(filtroTipo || undefined);
      setAgentes(data);
    } catch {
      toast.error('Erro ao carregar agentes.');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { carregar(); }, [filtroTipo]);

  const abrirNovo = () => {
    setEditando(null);
    setForm(emptyForm);
    setShowModal(true);
  };

  const abrirEditar = (a) => {
    setEditando(a);
    setForm({ nome: a.nome, cnpj: a.cnpj, tipo: a.tipo, email: a.email || '', telefone: a.telefone || '' });
    setShowModal(true);
  };

  const fecharModal = () => { setShowModal(false); setEditando(null); };

  const salvar = async (e) => {
    e.preventDefault();
    setSaving(true);
    try {
      if (editando) {
        await agenteService.atualizar(editando.id, form);
        toast.success('Agente atualizado!');
      } else {
        await agenteService.criar(form);
        toast.success('Agente cadastrado!');
      }
      fecharModal();
      carregar();
    } catch (err) {
      toast.error(err.response?.data?.message || 'Erro ao salvar agente.');
    } finally {
      setSaving(false);
    }
  };

  const deletar = async () => {
    try {
      await agenteService.deletar(confirmDelete.id);
      toast.success('Agente removido.');
      setConfirmDelete(null);
      carregar();
    } catch (err) {
      toast.error(err.response?.data?.message || 'Erro ao remover agente.');
    }
  };

  const TipoIcon = ({ tipo }) =>
    tipo === 'BANCO'
      ? <Landmark size={14} style={{ marginRight: 4 }} />
      : <Building2 size={14} style={{ marginRight: 4 }} />;

  return (
    <div className="page">
      <div className="page-header">
        <div>
          <h2 className="page-title">Agentes</h2>
          <p className="page-subtitle">Empresas e bancos que analisam pedidos e concedem crédito</p>
        </div>
        <button className="btn btn-primary" onClick={abrirNovo}>
          <Plus size={16} /> Novo Agente
        </button>
      </div>

      {/* Filtro */}
      <div className="filter-bar">
        <label>Filtrar por tipo:</label>
        <select value={filtroTipo} onChange={e => setFiltroTipo(e.target.value)} className="input" style={{ width: 160 }}>
          <option value="">Todos</option>
          <option value="EMPRESA">Empresa</option>
          <option value="BANCO">Banco</option>
        </select>
      </div>

      {/* Tabela */}
      {loading ? (
        <p className="empty-state">Carregando...</p>
      ) : agentes.length === 0 ? (
        <p className="empty-state">Nenhum agente encontrado.</p>
      ) : (
        <div className="table-wrapper">
          <table className="table">
            <thead>
              <tr>
                <th>Nome</th>
                <th>CNPJ</th>
                <th>Tipo</th>
                <th>E-mail</th>
                <th>Telefone</th>
                <th>Ações</th>
              </tr>
            </thead>
            <tbody>
              {agentes.map(a => (
                <tr key={a.id}>
                  <td><strong>{a.nome}</strong></td>
                  <td>{a.cnpj}</td>
                  <td>
                    <span className={`badge badge-${a.tipo === 'BANCO' ? 'info' : 'warning'}`}>
                      <TipoIcon tipo={a.tipo} />
                      {TIPO_LABELS[a.tipo]}
                    </span>
                  </td>
                  <td>{a.email || '—'}</td>
                  <td>{a.telefone || '—'}</td>
                  <td>
                    <div className="action-buttons">
                      <button className="btn btn-ghost btn-sm" onClick={() => abrirEditar(a)} title="Editar">
                        <Pencil size={14} />
                      </button>
                      <button className="btn btn-ghost btn-sm btn-danger" onClick={() => setConfirmDelete(a)} title="Remover">
                        <Trash2 size={14} />
                      </button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

      {/* Modal Cadastro/Edição */}
      {showModal && (
        <div className="modal-overlay" onClick={fecharModal}>
          <div className="modal" onClick={e => e.stopPropagation()}>
            <div className="modal-header">
              <h3>{editando ? 'Editar Agente' : 'Novo Agente'}</h3>
              <button className="modal-close" onClick={fecharModal}>×</button>
            </div>
            <form onSubmit={salvar} className="modal-body">
              <div className="form-group">
                <label>Nome *</label>
                <input className="input" required value={form.nome}
                  onChange={e => setForm(f => ({ ...f, nome: e.target.value }))} />
              </div>
              <div className="form-group">
                <label>CNPJ *</label>
                <input className="input" required placeholder="00.000.000/0001-00"
                  value={form.cnpj}
                  onChange={e => setForm(f => ({ ...f, cnpj: e.target.value }))} />
              </div>
              <div className="form-group">
                <label>Tipo *</label>
                <select className="input" required value={form.tipo}
                  onChange={e => setForm(f => ({ ...f, tipo: e.target.value }))}>
                  <option value="EMPRESA">Empresa</option>
                  <option value="BANCO">Banco</option>
                </select>
              </div>
              <div className="form-row">
                <div className="form-group">
                  <label>E-mail</label>
                  <input className="input" type="email" value={form.email}
                    onChange={e => setForm(f => ({ ...f, email: e.target.value }))} />
                </div>
                <div className="form-group">
                  <label>Telefone</label>
                  <input className="input" value={form.telefone}
                    onChange={e => setForm(f => ({ ...f, telefone: e.target.value }))} />
                </div>
              </div>
              <div className="modal-footer">
                <button type="button" className="btn btn-ghost" onClick={fecharModal}>Cancelar</button>
                <button type="submit" className="btn btn-primary" disabled={saving}>
                  {saving ? 'Salvando...' : editando ? 'Salvar' : 'Cadastrar'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}

      {/* Confirm Delete */}
      {confirmDelete && (
        <ConfirmModal
          title="Remover Agente"
          message={`Deseja remover o agente "${confirmDelete.nome}"? Esta ação não pode ser desfeita.`}
          onConfirm={deletar}
          onCancel={() => setConfirmDelete(null)}
        />
      )}
    </div>
  );
}
