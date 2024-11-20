package com.example.wonjang.model;


import com.example.wonjang.dto.SignUpDto;
import com.example.wonjang.dto.UpdateMemberDto;
import com.example.wonjang.dto.UserDto;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.*;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@ToString
@NoArgsConstructor
@Getter
@Builder
@Entity
@AllArgsConstructor
public class Member extends BaseTimeEntity implements UserDetails {  // UserDetails를 상속받아 인증 객체로 사용
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    private String name;
    private String degree;
    private String grade;
    private String password;
    private String picture;
    private String provider;
    private String mobile;
    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.USER;
    @Builder.Default
    @Column(name = "isEnabled", columnDefinition = "VARCHAR(1) default 'N'")
    private String isEnabled = "N";

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<Inquiry> inquiries = new ArrayList<>();

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<Feedback> feedbacks = new ArrayList<>();

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    private RegisterCode registerCode;

    public boolean isFamily(){
        return this.role.getKey().equals(Role.FAMILY.getKey());
    }
    @Override  // 권한 반환
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_".concat(this.role.name())));
    }

    // 사용자의 id를 반환 (고유 값)
    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    // 계정 만료 여부 반환
    @Override
    public boolean isAccountNonExpired() {
        // 만료되었는지 확인하는 로직
        return true;  // true -> 만료되지 않았음
    }

    // 계정 잠금 여부 반환
    @Override
    public boolean isAccountNonLocked() {
        // 계정 잠금 여부 확인 로직
        return true;  // true -> 잠금 X
    }

    // 패스워드의 만료 여부 반환
    @Override
    public boolean isCredentialsNonExpired() {
        // 패스워드 만료 여부 확인
        return true;  // true -> 만료 X
    }

    // 계정 사용 가능 여부 반환
    @Override
    public boolean isEnabled() {
        // 계정이 사용 가능한지 확인하는 로직
        return Objects.equals(this.isEnabled, "Y");  // true -> 사용 O
    }

    public UserDto toDto() {
        return UserDto.builder().name(this.name).email(this.email).role(this.role).build();
    }

    public Member update(String name, String picture) {
        this.name = name;
        this.picture = picture;
        return this;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }

    public void updateActive(Boolean isActive) {
        this.isEnabled = isActive ? "Y" : "N";
    }

    public SignUpDto toSignUpDto() {
        return SignUpDto.builder()
                .name(this.name)
                .email(this.email)
                .picture(this.picture)
                .grade(this.grade)
                .degree(this.degree)
                .mobile(this.mobile)
                .build();
    }

    public void updateMember(@Valid UpdateMemberDto updateMemberDto) {
        this.name = updateValue(this.name, updateMemberDto.getName());
        this.degree = updateValue(this.degree, updateMemberDto.getDegree());
        this.grade = updateValue(this.grade, updateMemberDto.getGrade());
        this.picture = updateValue(this.picture, updateMemberDto.getPicture());
        this.password = updateValue(this.password, updateMemberDto.getPassword());
        this.mobile = updateValue(this.password, updateMemberDto.getMobile());
    }

    private String updateValue(String currentValue, String newValue) {
        if (StringUtils.isNotEmpty(newValue) && !Objects.equals(currentValue, newValue)) {
            return newValue;
        }
        return currentValue;
    }

    public void updatePicture(String picture) {
        this.picture = updateValue(this.picture,picture);
    }
}
