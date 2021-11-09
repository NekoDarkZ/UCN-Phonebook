package cl.ucn.disc.dsm.smurquio.ucnphonebook;

import lombok.Builder;
import lombok.Getter;

/**
 * The UCN Funcionario.
 *
 * @author Sebastian Murquio Castillo
 */
@Builder
public final class Funcionario {
  /**
   * The ID.
   */
  @Getter
  private int id;

  /**
   * The nombre.
   */
  @Getter
  private final String nombre;

  /**
   * The cargo.
   */
  @Getter
  private final String cargo;

  /**
   * The cargo.
   */
  @Getter
  private final String unidad;

  /**
   * The cargo.
   */
  @Getter
  private final String email;

  /**
   * The telefono.
   */
  @Getter
  private final String telefono;

  /**
   * The oficina.
   */
  @Getter
  private final String oficina;

  /**
   * The direccion.
   */
  @Getter
  private final String direccion;

}