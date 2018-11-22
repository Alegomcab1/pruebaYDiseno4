
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.Actor;

@Repository
public interface ActorRepository extends JpaRepository<Actor, Integer> {

	// Si el actor no esta registrado, que se pueda registrar como customer, como handy worker o
	// como sponsor.

	// Si el actor esta registrado, podrá editar su personal data
	// Intercambiar mensajes con otros actores y organizarlos
	// Manage las boxes excepto las de sistema(Enviado, recibido, basura, spam)
	// Un sponsor debe: ver el catalogo de tutoriales, Los actores deben de poder ver los profile de los
	// Handy workers, incluyendo su personal data y una lista de tutoriales que hayan escrito
}
