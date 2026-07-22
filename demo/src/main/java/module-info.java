module pe.utp.sifacsw {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.graphics;
    requires javafx.web;
    requires itextpdf;
    requires java.desktop;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fontawesome5;
    requires java.mail;
    requires com.zaxxer.hikari;

    // Presentacion - Controladores necesarios para JavaFX reflection
    opens pe.utp.sifacsw.presentacion.controladores to javafx.fxml;
    exports pe.utp.sifacsw.presentacion.controladores;

    // Presentacion - Core de la aplicacion
    opens pe.utp.sifacsw.presentacion to javafx.fxml;
    exports pe.utp.sifacsw.presentacion;

    // Presentacion - Componentes UI
    exports pe.utp.sifacsw.presentacion.componentes;

    // Dominio - Modelos de negocio
    exports pe.utp.sifacsw.dominio.modelos;

    // Aplicacion - Servicios de logica de negocio
    exports pe.utp.sifacsw.aplicacion.servicios;

    // Infraestructura - Acceso a datos
    exports pe.utp.sifacsw.infraestructura.dao;
    exports pe.utp.sifacsw.infraestructura.persistencia;
    exports pe.utp.sifacsw.infraestructura.database;

    // Patrones de Diseno
    exports pe.utp.sifacsw.patrones.builder;
    exports pe.utp.sifacsw.patrones.adapter;
    exports pe.utp.sifacsw.patrones.strategy;
}
