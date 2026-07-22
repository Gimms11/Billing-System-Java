package pe.utp.sifacsw.infraestructura.dao;

import pe.utp.sifacsw.dominio.modelos.Departamento;
import pe.utp.sifacsw.dominio.modelos.Provincia;
import pe.utp.sifacsw.dominio.modelos.Distrito;
import java.util.List;

public interface DAOUbigeo {
    List<Departamento> obtenerTodosDepartamentos();
    List<Provincia> obtenerProvinciasPorDepartamento(Long idDepartamento);
    List<Distrito> obtenerDistritosPorProvincia(Long idProvincia);
    Distrito obtenerDistrito(Long id);
    List<Long> obtenerIds(Long idDistrito);
}

