package pe.utp.sifacsw.aplicacion.servicios;

import pe.utp.sifacsw.dominio.modelos.Departamento;
import pe.utp.sifacsw.dominio.modelos.Provincia;
import pe.utp.sifacsw.dominio.modelos.Distrito;

import java.util.List;

import pe.utp.sifacsw.infraestructura.dao.DAOFactory;
import pe.utp.sifacsw.infraestructura.dao.DAOUbigeo;

public class UbigeoService {
    private final DAOUbigeo repository;

    public UbigeoService() {
        DAOFactory factory = DAOFactory.getDAOFactory();
        this.repository = factory.getUbigeoDAO();
    }

    public List<Departamento> cargarDepartamentos() {
        return repository.obtenerTodosDepartamentos();
    }

    public List<Provincia> cargarProvincias(Long idDepartamento) {
        if (idDepartamento == null) return List.of();
        return repository.obtenerProvinciasPorDepartamento(idDepartamento);
    }

    public List<Distrito> cargarDistritos(Long idProvincia) {
        if (idProvincia == null) return List.of();
        return repository.obtenerDistritosPorProvincia(idProvincia);
    }

    public Distrito obtenerDistrito(Long id) {
        if (id == null) return null;
        return repository.obtenerDistrito(id);
    }

    public List<Long> obtenerIds(Long id){
        if (id == null) return null;
        return repository.obtenerIds(id);
    }
}

