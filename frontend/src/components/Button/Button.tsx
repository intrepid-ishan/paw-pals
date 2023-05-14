import React, { ReactNode } from 'react';
import Button from '@material-ui/core/Button';

interface CustomButtonProps {
  disabled?: boolean;
  onClick?: () => void;
  className?: string;
  type?: 'button' | 'submit' | 'reset' | undefined;
  color?: 'inherit' | 'primary' | 'secondary' | 'default' | undefined;
  children?: React.ReactNode;
  variant?: 'text' | 'outlined' | 'contained' | undefined;
  fullWidth?: boolean;
  startIcon?: ReactNode;
}

const CustomButton: React.FC<CustomButtonProps> = ({
  disabled,
  onClick,
  className,
  color,
  children,
  type,
  fullWidth = false,
  variant,
  startIcon
}) => {
  return (
    <Button
      type={type}
      variant={variant}
      color={color}
      disabled={disabled}
      fullWidth={fullWidth}
      className={className}
      onClick={onClick}
      startIcon={startIcon}
    >
      {children}
    </Button>
  );
};

export default CustomButton;
