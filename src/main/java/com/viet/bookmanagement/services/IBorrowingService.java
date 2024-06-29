package com.viet.bookmanagement.services;

import com.viet.bookmanagement.dtos.BorrowingDTO;
import com.viet.bookmanagement.dtos.UserBorrowingDTO;
import com.viet.bookmanagement.entities.Borrowing;

import java.util.List;

public interface IBorrowingService {

    void lend(Borrowing borrowing);
    public List<BorrowingDTO> getUsersWithDueDatesTodayAndTomorrow();
    public List<UserBorrowingDTO> getAllUsersByBorrowing();

    public void exportBorrowingsToExcel(List<BorrowingDTO> borrowingDTOs);


}
