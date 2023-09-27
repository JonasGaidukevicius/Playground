package lt.jonas.playground.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import lt.jonas.playground.exception.ConditionsForActionNotMetException;
import lt.jonas.playground.model.entity.Kid;
import lt.jonas.playground.model.dto.KidRequest;
import lt.jonas.playground.model.dto.TicketRequest;
import lt.jonas.playground.model.view.KidView;
import lt.jonas.playground.repository.KidRepository;
import lt.jonas.playground.utility.UniqueCodeGenerationUtility;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@CommonsLog
public class KidService {
    private final KidRepository kidRepository;

    @Transactional
    public KidView createKid(KidRequest kidRequest) {
        Kid kid = Kid.builder()
                .name(kidRequest.getName())
                .age(kidRequest.getAge())
                .customerCode(UniqueCodeGenerationUtility.generateUniqueCode())
                .build();
        kidRepository.save(kid);
        LOG.info(String.format("Created a new kid '%s' with customer code '%s'.", kid.getName(), kid.getCustomerCode()));
        return KidView.kidToKidView(kid);
    }

    @Transactional(readOnly = true)
    public List<KidView> getKids() {
        return kidRepository.findAll().stream().map(KidView::kidToKidView).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public KidView getKid(Long id) throws EntityNotFoundException {
        return kidRepository.findById(id).map(KidView::kidToKidView)
                .orElseThrow(() -> new EntityNotFoundException(String.format("No kid id=%s was found.", id)));
    }

    @Transactional
    public String buyTicket(TicketRequest ticketRequest) throws ConditionsForActionNotMetException, EntityNotFoundException {
        Kid kid = findKidByCustomerCode(ticketRequest.getCustomerCode());
        if (kid.getTicketNumber() != null) {
            throw new ConditionsForActionNotMetException(String.format("Kid customer code '%s' already has a ticket.", kid.getCustomerCode()));
        }
        String ticket = UniqueCodeGenerationUtility.generateUniqueCode();
        kid.setTicketNumber(ticket);
        kidRepository.save(kid);
        LOG.info(String.format("Bought a ticket '%s' for a kid '%s', customer code '%s'.", kid.getTicketNumber(), kid.getName(), kid.getCustomerCode()));
        return kid.getTicketNumber();
    }

    public Kid findKidByCustomerCode(String customerCode) throws EntityNotFoundException {
        return kidRepository.findByCustomerCode(customerCode)
                .orElseThrow(() -> new EntityNotFoundException(String.format("No customer-coded=%s was found.", customerCode)));
    }
}
