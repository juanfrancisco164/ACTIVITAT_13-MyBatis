package org.mybatis;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Scanner;

public class HotelReservationApp implements BookingMapper {
    public static Scanner scanner = new Scanner(System.in);
    private final String MYBATIS_CONFIG_PATH = "mybatis-config.xml";
    SqlSessionFactory sqlSessionFactory;
    SqlSession session;
    BookingMapper bookingMapper;

    public HotelReservationApp() throws IOException {
        this.sqlSessionFactory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsStream(MYBATIS_CONFIG_PATH));
        this.session = sqlSessionFactory.openSession();
        this.bookingMapper = session.getMapper(BookingMapper.class);
    }

    public static void main(String[] args) throws IOException {
        HotelReservationApp main = new HotelReservationApp();
        int option;

        do {
            main.printMainMenu();
            option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    System.out.print("Ruta del archivo XML: ");
                    String xmlFilePath = scanner.nextLine();
                    main.loadXmlData(xmlFilePath);
                    break;
                case 2:
                    System.out.print("Ruta del archivo CSV: ");
                    String csvFilePath = scanner.nextLine();
                    main.saveDataToCsv(csvFilePath);

                    break;
                case 3:
                    main.deleteAllBookings();
                    break;
                case 4:
                    main.insertBooking(main.createNewBooking());
                    break;
                case 5:
                    System.out.print("Inserte el id de la reserva que desea borrar" + "\n");
                    main.deleteBookingById(scanner.nextInt());
                    scanner.nextLine();
                    break;
                case 6:
                    System.out.println("Inserte el id de la reserva que desea actualizar:");
                    main.updateBooking(main.getBookingById(scanner.nextInt()));
                    scanner.nextLine();
                    break;
                case 0:
                    System.out.println("Adiooos");
                    break;
                default:
                    System.out.println("Invalid option. Please select a valid option.");
            }

        } while (option != 0);
    }

    private void printMainMenu() {
        System.out.println();
        System.out.print("Menú Principal:\n1. Cargar datos desde un archivo XML\n2. Guardar datos en un archivo CSV\n3. Eliminar todas las reservas\n4. Agregar una reserva\n5. Eliminar una reserva\n6. Modificar una reserva\n0. Salir\nSeleccione una opción: ");

    }

    private Booking createNewBooking() {
        Booking newBooking = new Booking();
        newBooking.setLocation_number(this.getLastLocationNumber() + 1);
        System.out.println("Creando nueva reserva.......");
        System.out.println("__________________________________________");
        System.out.print("Inserta el id del cliente:" + "\n");
        newBooking.setClientId(scanner.nextInt());
        scanner.nextLine();
        System.out.print("Inserta el nombre de cliente:" + "\n");
        newBooking.setClient(scanner.nextLine());
        System.out.print("Inserta el nombre de la agencia:" + "\n");
        newBooking.setAgency(scanner.nextLine());
        System.out.print("Inserta el id de la agencia:" + "\n");
        newBooking.setId_agency(scanner.nextInt());
        scanner.nextLine();
        System.out.print("Inserta el precio de la reserva:" + "\n");
        newBooking.setPrice(scanner.nextLine());
        scanner.nextLine();
        System.out.print("Inserta el id de la habitación:" + "\n");
        newBooking.setId_type(scanner.nextLine());
        System.out.print("Inserta el tipo de habitación:" + "\n");
        newBooking.setRoom(scanner.nextLine());
        System.out.print("Inserta el nombre del hotel:" + "\n");
        newBooking.setHotel(scanner.nextLine());
        System.out.print("Inserta el id del hotel:" + "\n");
        newBooking.setId_hotel(scanner.nextInt());
        scanner.nextLine();
        System.out.print("Inserta el número de noches:" + "\n");
        newBooking.setRoom_nights(scanner.nextInt());
        scanner.nextLine();
        System.out.print("Insera fecha de check-in:" + "\n");
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String date = scanner.nextLine();
        try {
            newBooking.setCheck_in(dateFormat.parse(date));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        System.out.println("__________________________________________");
        return newBooking;
    }

    @Override
    public void insertBooking(Booking booking) {
        try {
            bookingMapper.insertBooking(booking);
            session.commit();
            System.out.println("Reserva añadida ;D");
        } catch (Exception e) {
            session.rollback();
            e.printStackTrace();
            System.out.println("Opcion invalida. Pruebe de nuevo");
        }
    }

    @Override
    public void updateBooking(Booking booking) {
        try {
            System.out.println("Datos actuales de la reserva");
            printBooking(booking);

            int option;
            do {
                printEditMenu();

                option = scanner.nextInt();
                scanner.nextLine();

                switch (option) {
                    case 1:
                        System.out.print("Ingrese el nuevo Número de Ubicación: ");
                        int newLocationNumber = scanner.nextInt();
                        booking.setLocation_number(newLocationNumber);
                        scanner.nextLine();
                        break;
                    case 2:
                        System.out.print("Ingrese el nuevo ID de Cliente: ");
                        int newClientID = scanner.nextInt();
                        booking.setClientId(newClientID);
                        scanner.nextLine();
                        break;
                    case 3:
                        System.out.print("Ingrese el nuevo Cliente: ");
                        String newClient = scanner.nextLine();
                        booking.setClient(newClient);
                        break;
                    case 4:
                        System.out.print("Ingrese la nueva Agencia: ");
                        String newAgency = scanner.nextLine();
                        booking.setAgency(newAgency);
                        break;
                    case 5:
                        System.out.print("Ingrese el nuevo ID de la Agencia: ");
                        int newAgencyID = scanner.nextInt();
                        booking.setId_agency(newAgencyID);
                        scanner.nextLine();
                        break;
                    case 6:
                        System.out.print("Ingrese el nuevo Precio: ");
                        String newPrice = scanner.nextLine();
                        booking.setPrice(newPrice);
                        scanner.nextLine();
                        break;
                    case 7:
                        System.out.print("Ingrese el nuevo ID de Tipo: ");
                        String newTypeID = scanner.nextLine();
                        booking.setId_type(newTypeID);
                        break;
                    case 8:
                        System.out.print("Ingrese la nueva habitación: ");
                        String newRoom = scanner.nextLine();
                        booking.setRoom(newRoom);
                        break;
                    case 9:
                        System.out.print("Ingrese el nuevo ID del hotel: ");
                        int newHotelID = scanner.nextInt();
                        booking.setId_hotel(newHotelID);
                        scanner.nextLine();
                        break;
                    case 10:
                        System.out.print("Ingrese el nuevo hotel: ");
                        String newHotel = scanner.nextLine();
                        booking.setHotel(newHotel);
                        break;
                    case 11:
                        System.out.print("Ingrese la nueva fecha de check-in (aaaa/MM/dd): ");
                        String newCheckInDate = scanner.nextLine();
                        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                        try {
                            booking.setCheck_in(dateFormat.parse(newCheckInDate));
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    case 12:
                        System.out.print("Ingrese la nueva cantidad de noches de la habitación: ");
                        int newRoomNights = scanner.nextInt();
                        booking.setRoom_nights(newRoomNights);
                        scanner.nextLine();
                        break;
                    case 13:
                        break;
                    default:
                        System.out.println("Opcion invalida. Pruebe de nuevo");
                }
            } while (option != 13);

            bookingMapper.updateBooking(booking);
            session.commit();
            System.out.println("¡Reserva actualizada exitosamente!");
        } catch (Exception e) {
            session.rollback();
            e.printStackTrace();
            System.out.println("Error al actualizar la reserva. Deshaciendo cambios.");
        }
    }


    @Override
    public void deleteBookingById(int id) {
        try {
            bookingMapper.deleteBookingById(id);
            session.commit();
            System.out.println("Reserva eliminada exitosamente!");
        } catch (Exception e) {
            session.rollback();
            e.printStackTrace();
            System.out.println("Error al eliminar la reserva. Deshaciendo cambios.");
        }
    }

    @Override
    public void deleteAllBookings() {
        try {
            bookingMapper.deleteAllBookings();
            session.commit();
            System.out.println("Todas las reservas eliminadas exitosamente!");
        } catch (Exception e) {
            session.rollback();
            e.printStackTrace();
            System.out.println("Error al eliminar todas las reservas. Deshaciendo cambios.");
        }
    }

    @Override
    public List<Booking> getAllBookings() {
        try {
            return bookingMapper.getAllBookings();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Booking getBookingById(int bookingIdToModify) {
        try {
            return bookingMapper.getBookingById(bookingIdToModify);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int getLastLocationNumber() {
        return bookingMapper.getLastLocationNumber();
    }

    @Override
    public void loadXmlData(String xmlFilePath) {
        try {
            bookingMapper.loadXmlData(xmlFilePath);
            session.commit();
            System.out.println("Datos XML cargados exitosamente!");
        } catch (Exception e) {
            session.rollback();
            e.printStackTrace();
            System.out.println("Error al cargar datos XML. Deshaciendo cambios.");
        }
    }

    @Override
    public void saveDataToCsv(String csvFilePath) {
        try {
            bookingMapper.saveDataToCsv(csvFilePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void printBooking(Booking booking) {
        System.out.println("Detalles de la Reserva:");
        System.out.println("ID: " + booking.getId());
        System.out.println("Número de Ubicación: " + booking.getLocation_number());
        System.out.println("ID del Cliente: " + booking.getClientId());
        System.out.println("Cliente: " + booking.getClient());
        System.out.println("ID de la Agencia: " + booking.getId_agency());
        System.out.println("Agencia: " + booking.getAgency());
        System.out.println("Precio: " + booking.getPrice());
        System.out.println("ID del Tipo: " + booking.getId_type());
        System.out.println("Habitación: " + booking.getRoom());
        System.out.println("ID del Hotel: " + booking.getId_hotel());
        System.out.println("Hotel: " + booking.getHotel());
        System.out.println("Fecha de Check-In: " + booking.getCheck_in());
        System.out.println("Noches de Habitación: " + booking.getRoom_nights());
    }

    private void printEditMenu() {
        System.out.println("\nOpciones de Actualización:");
        System.out.println("1. Actualizar Número de Ubicación");
        System.out.println("2. Actualizar ID del Cliente");
        System.out.println("3. Actualizar Cliente");
        System.out.println("4. Actualizar Agencia");
        System.out.println("5. Actualizar ID de la Agencia");
        System.out.println("6. Actualizar Precio");
        System.out.println("7. Actualizar ID del Tipo");
        System.out.println("8. Actualizar Habitación");
        System.out.println("9. Actualizar ID del Hotel");
        System.out.println("10. Actualizar Hotel");
        System.out.println("11. Actualizar Fecha de Check-In");
        System.out.println("12. Actualizar Noches de Habitación");
        System.out.println("13. Finalizar Actualización");
        System.out.print("Seleccione una opción: ");
    }
}