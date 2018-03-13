package com.example.easyboot.repository;

/**
 * Created by sundarblack on 2/25/18.
 */
import com.example.easyboot.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {

}
