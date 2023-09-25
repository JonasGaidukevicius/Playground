package lt.jonas.playground.service;

import lombok.RequiredArgsConstructor;
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
public class KidService {
    private final KidRepository kidRepository;

    @Transactional
    public void createKid(KidRequest kidRequest) {
        Kid kid = Kid.builder()
                .name(kidRequest.getName())
                .age(kidRequest.getAge())
                .customerCode(UniqueCodeGenerationUtility.generateUniqueCode())
                .build();
        kidRepository.save(kid);
    }

    @Transactional(readOnly = true)
    public List<KidView> getKids() {
        return kidRepository.findAll().stream().map(KidView::kidToKidView).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public KidView getKid(Long id) {
        return kidRepository.findById(id).map(KidView::kidToKidView)
                .orElseThrow(() -> new EntityNotFoundException(String.format("No kid id=%s was found.", id)));
    }

    @Transactional
    public void buyTicket(TicketRequest ticketRequest) {
        Kid kid = findKidByCustomerCode(ticketRequest.getCustomerCode());
        if (kid != null) {
            String ticket = UniqueCodeGenerationUtility.generateUniqueCode();
            kid.setTicketNumber(ticket);
            kidRepository.save(kid);
        }
    }

    public Kid findKidByCustomerCode(String customerCode) {
        return kidRepository.findByCustomerCode(customerCode)
                .orElseThrow(() -> new EntityNotFoundException(String.format("No customer-coded=%s was found.", customerCode)));
    }
}
