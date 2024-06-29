package com.viet.bookmanagement.services.impl;

import com.viet.bookmanagement.Utils.Utils;
import com.viet.bookmanagement.dao.IBorrowingDAO;
import com.viet.bookmanagement.dtos.BorrowingDTO;
import com.viet.bookmanagement.dtos.UserBorrowingDTO;
import com.viet.bookmanagement.entities.Borrowing;
import com.viet.bookmanagement.repositoties.IBorrowingRepository;
import com.viet.bookmanagement.services.IBorrowingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class BorrowingServiceImpl implements IBorrowingService {

    private static final String FILE_PATH = "F:/Work/Projects/personal_finance/Testing/export_excel/";
    @Autowired
    private IBorrowingDAO borrowingDAO;

    @Autowired
    private IBorrowingRepository borrowingRepository;

    @Override
    public void lend(Borrowing borrowing) {
        borrowingDAO.save(borrowing);
    }

    @Override
    public List<BorrowingDTO> getUsersWithDueDatesTodayAndTomorrow() {
        List<Object[]> usersWithDueDates = borrowingRepository.getUsersWithDueDatesTodayAndTomorrow();
        List<BorrowingDTO> borrowingDTOs = new ArrayList<>();
        for (Object[] userData : usersWithDueDates) {
            BorrowingDTO borrowingDTO = new BorrowingDTO();
            borrowingDTO.setUsername((String) userData[0]);
            borrowingDTO.setEmail((String) userData[1]);
            borrowingDTO.setDueDate((Date) userData[2]);
            borrowingDTOs.add(borrowingDTO);
        }
        return borrowingDTOs;
    }

    @Override
    public List<UserBorrowingDTO> getAllUsersByBorrowing() {
        List<Object[]> usersByBorrowing = borrowingRepository.getAllUsersByBorrowing();

        List<UserBorrowingDTO> userBorrowingDTOs = new ArrayList<>();
        for (Object[] userData : usersByBorrowing) {
            UserBorrowingDTO userBorrowingDTO = new UserBorrowingDTO();
            userBorrowingDTO.setUsername((String) userData[0]);
            userBorrowingDTO.setCount((Long) userData[1]);
            userBorrowingDTOs.add(userBorrowingDTO);
        }
        return userBorrowingDTOs;

    }


    public void exportBorrowingsToExcel(List<BorrowingDTO> borrowingDTOs) {
        try {
            String fullFilePath = FILE_PATH + "due_soon" + ".xlsx";
            Utils.exportToExcel(borrowingDTOs, fullFilePath);
        } catch (IOException e) {
            e.printStackTrace(); // Handle IO exception appropriately
        }
    }


}
