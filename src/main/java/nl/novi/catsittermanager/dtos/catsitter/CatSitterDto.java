package nl.novi.catsittermanager.dtos.catsitter;

import nl.novi.catsittermanager.models.CatSitter;
import nl.novi.catsittermanager.models.Order;

import java.util.ArrayList;
import java.util.List;

public class CatSitterDto {
    public String about;
    public List<Order> orderList = new ArrayList<>();
    public List<CatSitter> catsitters = new ArrayList<>();
}
