/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import org.hibernate.search.annotations.*;
import play.db.jpa.*;

/**
 *
 * @author waxzce
 */
@Entity
@Indexed
public class Tw extends Model {

    @Field(index = Index.TOKENIZED)
    public String text;

    @Field(index = Index.TOKENIZED)
    public String username;
}
