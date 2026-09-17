package com.vityarthi.sms.service;

import com.vityarthi.sms.model.Student;
import java.io.IOException;
import java.util.List;

/**
 * Interface defining persistent storage contract (Abstraction).
 */
public interface StorageService {
    List<Student> loadStudents() throws IOException;
    void saveStudents(List<Student> students) throws IOException;
}
