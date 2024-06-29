package com.viet.bookmanagement.repositoties;

import java.util.List;

public interface IBorrowingRepository {

    public List<Object[]> getUsersWithDueDatesTodayAndTomorrow();

    public List<Object[]> getAllUsersByBorrowing();


}
