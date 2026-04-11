import { useEffect, useState } from 'react';
import { Plus, Pencil, Trash2, Search, Car, X } from 'lucide-react';
import toast from 'react-hot-toast';
import { automovelService } from '../services/api';
import ConfirmModal from '../components/ConfirmModal';

const EMPTY = { matricula: '', ano: '', marca: '', modelo: '', placa: '', valorDiaria: '' };

const MARCAS = ['Chevrolet','Fiat','Ford','Honda','Hyundai','Jeep','Nissan','Renault','Toyota','Volkswagen','Outros'];

function AutomovelModal({ automovel, onClose, onSave }) {
  const editing = !!automovel?.id;
  const [form, setForm] = useState(editing
    ? { ...automovel, ano: String(automovel.ano), valorDiaria: String(automovel.valorDiaria ?? '') }
    : { ...EMPTY }
  );
  const [loading, setLoading] = useState(false);

  const set = (k) => (e) => setForm(f => ({ ...f, [k]: e.target.value }));

  const submit = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      const payload = { ...form, ano: parseInt(form.ano, 10), valorDiaria: parseFloat(form.valorDiaria) };
      if (editing) {
        await automovelService.atualizar(automovel.id, payload);
        toast.success('Automóvel atualizado!');
      } else {
        await automovelService.criar(payload);
        toast.success('Automóvel cadastrado!');
      }
      onSave();
    } catch (err) {
      toast.error(err.response?.data?.message || 'Erro ao salvar.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="modal-backdrop" onClick={onClose}>
      <div className="modal modal-lg" onClick={e => e.stopPropagation()}>
        <div className="modal-header">
          <h2>{editing ? 'Editar Automóvel' : 'Novo Automóvel'}</h2>
          <button className="btn-icon" onClick={onClose}><X size={16} /></button>
        </div>
        <div className="modal-body">
          <form onSubmit={submit}>
            <div className="form-grid cols-3">
              <div className="form-group">
                <label>Marca *</label>
                <select className="form-control" value={form.marca} onChange={set('marca')} required>
                  <option value="">Selecione...</option>
                  {MARCAS.map(m => <option key={m} value={m}>{m}</option>)}
                </select>
              </div>
              <div className="form-group">
                <label>Modelo *</label>
                <input className="form-control" value={form.modelo} onChange={set('modelo')} placeholder="Ex: Corolla" required />
              </div>
              <div className="form-group">
                <label>Ano *</label>
                <input className="form-control" type="number" min="1900" max="2030" value={form.ano} onChange={set('ano')} placeholder="2023" required />
              </div>
              <div className="form-group">
                <label>Placa *</label>
                <input className="form-control" value={form.placa} onChange={set('placa')} placeholder="BRA2E24" required />
              </div>
              <div className="form-group">
                <label>Matrícula *</label>
                <input className="form-control" value={form.matricula} onChange={set('matricula')} placeholder="ABC-0001" required />
              </div>
              <div className="form-group">
                <label>Valor da Diária (R$) *</label>
                <input className="form-control" type="number" min="0.01" step="0.01" value={form.valorDiaria}
                  onChange={set('valorDiaria')} placeholder="150.00" required />
              </div>
            </div>
            <div className="form-actions">
              <button type="button" className="btn btn-ghost" onClick={onClose}>Cancelar</button>
              <button type="submit" className="btn btn-primary" disabled={loading}>
                {loading ? 'Salvando...' : editing ? 'Salvar Alterações' : 'Cadastrar Automóvel'}
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
}

export default function Automoveis() {
  const [automoveis, setAutomoveis] = useState([]);
  const [loading, setLoading] = useState(true);
  const [search, setSearch] = useState('');
  const [filtroDisp, setFiltroDisp] = useState(false);
  const [modal, setModal] = useState(null);
  const [deleting, setDeleting] = useState(null);
  const [delLoading, setDelLoading] = useState(false);

  const load = () => {
    setLoading(true);
    automovelService.listar(filtroDisp)
      .then(r => setAutomoveis(r.data))
      .catch(() => toast.error('Erro ao carregar automóveis.'))
      .finally(() => setLoading(false));
  };

  useEffect(load, [filtroDisp]);

  const filtered = automoveis.filter(a =>
    `${a.marca} ${a.modelo} ${a.placa} ${a.matricula}`.toLowerCase().includes(search.toLowerCase())
  );

  const handleDelete = async () => {
    setDelLoading(true);
    try {
      await automovelService.deletar(deleting.id);
      toast.success('Automóvel removido.');
      setDeleting(null);
      load();
    } catch (err) {
      toast.error(err.response?.data?.message || 'Erro ao remover.');
    } finally {
      setDelLoading(false);
    }
  };

  return (
    <div className="page">
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: '1.5rem' }}>
        <div>
          <h2 style={{ fontSize: '1.25rem', fontWeight: 700, color: 'var(--gray-900)' }}>Automóveis</h2>
          <p style={{ color: 'var(--gray-500)', fontSize: '.875rem' }}>Gerencie a frota disponível para aluguel</p>
        </div>
        <button className="btn btn-primary" onClick={() => setModal('create')}>
          <Plus size={16} /> Novo Automóvel
        </button>
      </div>

      <div className="panel">
        <div className="panel-header">
          <span className="panel-title">{filtered.length} veículo{filtered.length !== 1 ? 's' : ''}</span>
          <div style={{ display: 'flex', gap: '.75rem', alignItems: 'center' }}>
            <label style={{ display: 'flex', alignItems: 'center', gap: '.4rem', fontSize: '.82rem', fontWeight: 500, color: 'var(--gray-600)', cursor: 'pointer' }}>
              <input type="checkbox" checked={filtroDisp} onChange={e => setFiltroDisp(e.target.checked)} />
              Só disponíveis
            </label>
            <div className="search-bar">
              <Search size={15} className="search-icon" />
              <input
                className="form-control"
                placeholder="Buscar por marca, modelo, placa..."
                value={search}
                onChange={e => setSearch(e.target.value)}
              />
            </div>
          </div>
        </div>

        {loading ? (
          <div className="loading-center"><div className="spinner" /></div>
        ) : filtered.length === 0 ? (
          <div className="empty-state">
            <Car size={36} />
            <p>{search ? 'Nenhum veículo encontrado.' : 'Nenhum automóvel cadastrado ainda.'}</p>
          </div>
        ) : (
          <div className="table-wrap">
            <table>
              <thead>
                <tr>
                  <th>#</th>
                  <th>Marca / Modelo</th>
                  <th>Ano</th>
                  <th>Placa</th>
                  <th>Matrícula</th>
                  <th>Diária</th>
                  <th>Status</th>
                  <th>Ações</th>
                </tr>
              </thead>
              <tbody>
                {filtered.map(a => (
                  <tr key={a.id}>
                    <td style={{ color: 'var(--gray-400)', fontSize: '.8rem' }}>{a.id}</td>
                    <td>
                      <div style={{ fontWeight: 500 }}>{a.marca} {a.modelo}</div>
                    </td>
                    <td>{a.ano}</td>
                    <td>
                      <span style={{ fontFamily: 'monospace', background: 'var(--gray-100)', padding: '.15rem .45rem', borderRadius: 4, fontSize: '.82rem' }}>
                        {a.placa}
                      </span>
                    </td>
                    <td>{a.matricula}</td>
                    <td>{a.valorDiaria != null ? new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(a.valorDiaria) : '—'}</td>
                    <td>
                      <span className={`badge ${a.disponivel ? 'badge-green' : 'badge-red'}`}>
                        {a.disponivel ? 'Disponível' : 'Indisponível'}
                      </span>
                    </td>
                    <td>
                      <div className="td-actions">
                        <button className="btn-icon" title="Editar" onClick={() => setModal(a)}>
                          <Pencil size={15} />
                        </button>
                        <button className="btn-icon danger" title="Remover" onClick={() => setDeleting(a)}>
                          <Trash2 size={15} />
                        </button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>

      {modal && (
        <AutomovelModal
          automovel={modal === 'create' ? null : modal}
          onClose={() => setModal(null)}
          onSave={() => { setModal(null); load(); }}
        />
      )}

      {deleting && (
        <ConfirmModal
          title="Remover Automóvel"
          message={`Remover o veículo ${deleting.marca} ${deleting.modelo} (${deleting.placa})?`}
          onConfirm={handleDelete}
          onCancel={() => setDeleting(null)}
          loading={delLoading}
        />
      )}
    </div>
  );
}
