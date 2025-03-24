package lk.ijse.worksphere.service.impl;

import lk.ijse.worksphere.dto.LeaveRequestDTO;
import lk.ijse.worksphere.entity.LeaveRequest;
import lk.ijse.worksphere.repository.LeaveRepo;
import lk.ijse.worksphere.service.LeaveService;
import lk.ijse.worksphere.util.IdGenerator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Author: Chanuka Prabodha
 * Date: 2025-03-23
 * Time: 02:51 PM
 */

@Service
public class LeaveServiceImpl implements LeaveService {

    @Autowired
    private LeaveRepo leaveRepo;

    @Autowired
    private ModelMapper modelMapper;

    public void saveLeave(LeaveRequestDTO leaveRequestDTO) {
        String generatedId;
        do {
            generatedId = IdGenerator.generateId("LV-");
        } while (leaveRepo.existsById(generatedId));
        leaveRequestDTO.setId(generatedId);
        try {
            leaveRepo.save(modelMapper.map(leaveRequestDTO, LeaveRequest.class));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
